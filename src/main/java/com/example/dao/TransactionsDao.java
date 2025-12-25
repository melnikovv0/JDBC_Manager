package com.example.dao;

import com.example.db.Database;
import com.example.entity.OrderItem;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionsDao {

    public static int callFinishOrder(
            Database db,
            int userId,
            int restaurantId,
            int courierId,
            List<OrderItem> items,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String status
    ) throws SQLException {

        String sql = "{call FinishOrder(?, ?, ?, ?, ?, ?, ?, ?)}";

        SQLServerDataTable itemTable = new SQLServerDataTable();
        itemTable.addColumnMetadata("id_food", java.sql.Types.INTEGER);
        itemTable.addColumnMetadata("quantity", java.sql.Types.INTEGER);
        itemTable.addColumnMetadata("price_at_time_of_order", java.sql.Types.DECIMAL);

        for (OrderItem item : items) {
            itemTable.addRow(item.getIdFood(), item.getQuantity(), item.getPriceAtTimeOfOrder());
        }

        Connection conn = db.getConnection();

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, restaurantId);
            stmt.setInt(3, courierId);
            stmt.setObject(4, itemTable);
            stmt.setTimestamp(5, Timestamp.valueOf(startTime));
            stmt.setTimestamp(6, Timestamp.valueOf(endTime));
            stmt.setString(7, status);
            stmt.registerOutParameter(8, java.sql.Types.INTEGER);

            stmt.execute();

            return stmt.getInt(8);
        }
    }

    public static int createOrderManually(
            Database db,
            int userId,
            int restaurantId,
            int courierId,
            List<OrderItem> items,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String status
    ) throws SQLException {

        Connection conn = db.getConnection();
        int orderId = -1;

        try {
            db.beginTransaction();

            BigDecimal totalPrice = BigDecimal.ZERO;
            for (OrderItem item : items) {
                totalPrice = totalPrice.add(item.getPriceAtTimeOfOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            String insertOrder = """
                    INSERT INTO [Order] 
                        (id_user, id_courier, id_restaurant, order_details, status, start_time, end_time, price)
                    VALUES (?, ?, ?, NULL, ?, ?, ?, ?)
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, courierId);
                stmt.setInt(3, restaurantId);
                stmt.setString(4, status);
                stmt.setTimestamp(5, Timestamp.valueOf(startTime));
                stmt.setTimestamp(6, Timestamp.valueOf(endTime));
                stmt.setBigDecimal(7, totalPrice);

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("No order ID returned.");
                    }
                }
            }

            String insertDetail = """
                    INSERT INTO Order_Details (id_order, id_food, quantity, price_at_time_of_order)
                    VALUES (?, ?, ?, ?)
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(insertDetail)) {
                for (OrderItem item : items) {
                    stmt.setInt(1, orderId);
                    stmt.setInt(2, item.getIdFood());
                    stmt.setInt(3, item.getQuantity());
                    stmt.setBigDecimal(4, item.getPriceAtTimeOfOrder());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            db.endTransaction();
            return orderId;

        } catch (SQLException e) {
            db.rollback();
            throw e;
        }
    }
}

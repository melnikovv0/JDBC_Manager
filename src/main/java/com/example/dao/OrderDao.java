package com.example.dao;

import com.example.db.Database;
import com.example.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDao {

    public static int insertOrder(Database db, Order order) throws SQLException {
        String sql = """
                INSERT INTO [Order] 
                    (id_user, id_courier, id_restaurant, order_details, status, start_time, end_time, price)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection conn = db.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getIdUser());

            if (order.getIdCourier() != null) {
                stmt.setInt(2, order.getIdCourier());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            stmt.setInt(3, order.getIdRestaurant());
            stmt.setString(4, order.getOrderDetails());
            stmt.setString(5, order.getStatus());
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(order.getStartTime()));
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(order.getEndTime()));
            stmt.setDouble(8, order.getPrice());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(" Order insert failed, no rows affected.");
            }

            try (var rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    order.setIdOrder(id);
                    return id;
                } else {
                    throw new SQLException(" Order insert failed, no ID returned.");
                }
            }

        }
    }
}

package com.example.app;

import com.example.dao.TransactionsDao;
import com.example.db.Database;
import com.example.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        Database db = new Database();

        try {
            db.connect();

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem(1, 2, new BigDecimal("500.00"))); //

            int orderId1 = TransactionsDao.callFinishOrder(
                    db,
                    1,
                    1,
                    1,
                    items,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(60),
                    "Delivered"
            );
            System.out.println(" FinishOrder created Order with ID: " + orderId1);

            int orderId2 = TransactionsDao.createOrderManually(
                    db,
                    1,
                    1,
                    1,
                    items,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(60),
                    "Delivered"
            );
            System.out.println(" Manual transaction created Order with ID: " + orderId2);

        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}

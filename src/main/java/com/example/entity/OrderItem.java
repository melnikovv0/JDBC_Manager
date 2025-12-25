package com.example.entity;

import java.math.BigDecimal;

public class OrderItem {
    private int idFood;
    private int quantity;
    private BigDecimal priceAtTimeOfOrder;

    public OrderItem(int idFood, int quantity, BigDecimal priceAtTimeOfOrder) {
        this.idFood = idFood;
        this.quantity = quantity;
        this.priceAtTimeOfOrder = priceAtTimeOfOrder;
    }

    public int getIdFood() {
        return idFood;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceAtTimeOfOrder() {
        return priceAtTimeOfOrder;
    }
}

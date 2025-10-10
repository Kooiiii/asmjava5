package com.poly.asm.entity;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private User user;
    private Double totalPrice;
    private String status;

    public Order(Long id, User user, Double totalPrice, String status) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}

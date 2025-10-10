package com.poly.asm.entity;

import lombok.Data;

@Data
public class CartItem {
    private Long id;
    private Product product;
    private Integer quantity;

    public CartItem(Long id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }
}
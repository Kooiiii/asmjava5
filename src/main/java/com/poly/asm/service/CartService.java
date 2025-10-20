package com.poly.asm.service;

import com.poly.asm.entity.CartItem;
import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(Integer userId);
    CartItem addToCart(Integer userId, Integer productVariantId, int quantity);
    CartItem updateCartItem(Integer cartItemId, int quantity);
    void removeCartItem(Integer cartItemId);
    void clearCart(Integer userId);
    void removeCartItem(Integer cartItemId, Integer userId);
}
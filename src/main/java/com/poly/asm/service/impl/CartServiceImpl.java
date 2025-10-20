package com.poly.asm.service.impl;

import com.poly.asm.dao.CartItemRepository;
import com.poly.asm.dao.ProductVariantRepository;
import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.CartItem;
import com.poly.asm.entity.ProductVariant;
import com.poly.asm.entity.User;
import com.poly.asm.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductVariantRepository productVariantRepository;

    @Override
    public List<CartItem> getCartItems(Integer userId) {
        return cartItemRepository.findByCustomerId(userId);
    }

    @Override
    @Transactional
    public CartItem addToCart(Integer userId, Integer productVariantId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ProductVariant variant = productVariantRepository.findById(productVariantId).orElseThrow(() -> new RuntimeException("Product variant not found"));

        if (variant.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<CartItem> existingCartItem = cartItemRepository.findByCustomerIdAndProductVariantId(userId, productVariantId);

        if (existingCartItem.isPresent()) {
            // Nếu có, cập nhật số lượng
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        } else {
            // Nếu chưa, tạo mới
            CartItem newCartItem = new CartItem();
            newCartItem.setCustomer(user);
            newCartItem.setProductVariant(variant);
            newCartItem.setQuantity(quantity);
            return cartItemRepository.save(newCartItem);
        }
    }

    @Override
    public CartItem updateCartItem(Integer cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void removeCartItem(Integer cartItemId) {

    }

    @Override
    public void removeCartItem(Integer cartItemId, Integer userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Kiểm tra quyền sở hữu
        if (!cartItem.getCustomer().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(Integer userId) {
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(userId);
        cartItemRepository.deleteAll(cartItems);
    }
}
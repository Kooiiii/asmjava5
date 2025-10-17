package com.poly.asm.service;

import com.poly.asm.entity.Order;
import java.util.List;

public interface OrderService {
    /**
     * Tạo một đơn hàng mới từ giỏ hàng của người dùng.
     * @param userId Mã của người dùng đang đặt hàng.
     * @param addressId Mã địa chỉ giao hàng.
     * @return Đơn hàng đã được tạo.
     */
    Order createOrder(Integer userId, Integer addressId);

    // Chức năng cho người dùng
    List<Order> findByUserId(Integer userId);

    // Chức năng cho Admin
    List<Order> findAllOrders();
    Order updateOrderStatus(Integer orderId, String status);
}
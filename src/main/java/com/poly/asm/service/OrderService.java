package com.poly.asm.service;

import com.poly.asm.entity.Order;
import java.util.List;
import java.util.Optional; // ✅ Import Optional

public interface OrderService {

    Order createOrder(Integer userId, Integer addressId);
    List<Order> findByUserId(Integer userId);
    List<Order> findAllOrders();
    Order updateOrderStatus(Integer orderId, String status);

    // ✅ Thêm hàm tìm theo ID (Admin Controller cần)
    Order findById(Integer id);

    // ✅ Thêm hàm xóa theo ID (Admin Controller cần)
    void deleteById(Integer id);
}
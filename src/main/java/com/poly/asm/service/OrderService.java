package com.poly.asm.service;

import com.poly.asm.entity.Order;
import com.poly.asm.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    // Tạo đơn hàng mới
    Order createOrder(Integer userId, Integer addressId);

    // Lấy danh sách đơn hàng của một user (dùng cho khách hàng)
    List<Order> findByUserId(Integer userId);

    // Lấy tất cả đơn hàng (dùng cho admin)
    List<Order> findAllOrders();

    // Cập nhật trạng thái đơn hàng
    Order updateOrderStatus(Integer orderId, String status);

    // Tìm đơn hàng theo ID (admin có thể truy cập)
    Order findById(Integer id);

    // Xóa đơn hàng
    void deleteById(Integer id);

    // ✅ Tìm đơn hàng theo ID và UserID (dùng cho /order/detail/{orderId})
    Optional<Order> findByIdAndUserId(Integer orderId, Integer userId);

    // ✅ Tính tổng giá trị đơn hàng (hiển thị ở trang chi tiết)
    BigDecimal calculateTotal(Order order);
}

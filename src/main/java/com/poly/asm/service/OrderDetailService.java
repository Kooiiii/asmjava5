package com.poly.asm.service;

import com.poly.asm.entity.Order;
import com.poly.asm.entity.OrderDetail;
import java.util.List;

public interface OrderDetailService {

    // ✅ Lưu chi tiết hóa đơn
    OrderDetail save(OrderDetail detail);

    // ✅ Lấy danh sách chi tiết theo hóa đơn
    List<OrderDetail> findByOrder(Order order);

    // ✅ Lấy danh sách chi tiết theo ID hóa đơn (dùng cho admin)
    List<OrderDetail> findByOrderId(Integer orderId);

    // ✅ Xóa toàn bộ chi tiết theo hóa đơn (nếu cần hủy)
    void deleteByOrderId(Integer orderId);
}

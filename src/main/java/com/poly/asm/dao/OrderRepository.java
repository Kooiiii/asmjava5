package com.poly.asm.dao;

import com.poly.asm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Tìm tất cả đơn hàng của một khách hàng.
    List<Order> findByCustomerId(Integer customerId);
    // Tìm các đơn hàng theo trạng thái
    List<Order> findByStatus(String status);
    // Tìm các đơn hàng trong một khoảng thời gian.
    List<Order> findByOrderDateBetween(Date startDate, Date endDate);
}

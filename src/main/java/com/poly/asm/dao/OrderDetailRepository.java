package com.poly.asm.dao;

import com.poly.asm.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    // tìm tất cả chi tiết hóa đơn theo mã hóa đơn
    List<OrderDetail> findByOrderId(Integer orderId);
}
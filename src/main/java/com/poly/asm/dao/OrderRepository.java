package com.poly.asm.dao;

import com.poly.asm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 🟩 Lấy đơn hàng theo ID + UserID (kèm chi tiết, sản phẩm, màu, size)
    @Query("""
        SELECT DISTINCT o FROM Order o
        LEFT JOIN FETCH o.orderDetails d
        LEFT JOIN FETCH d.productVariant v
        LEFT JOIN FETCH v.product p
        LEFT JOIN FETCH v.color c
        LEFT JOIN FETCH v.size s
        WHERE o.id = :orderId AND o.customer.id = :userId
    """)
    Optional<Order> findByIdAndCustomerIdWithDetails(
            @Param("orderId") Integer orderId,
            @Param("userId") Integer userId
    );

    // 🟩 Lấy toàn bộ đơn hàng của 1 user (kèm chi tiết, sản phẩm, màu, size)
    @Query("""
        SELECT DISTINCT o FROM Order o
        LEFT JOIN FETCH o.orderDetails d
        LEFT JOIN FETCH d.productVariant v
        LEFT JOIN FETCH v.product p
        LEFT JOIN FETCH v.color c
        LEFT JOIN FETCH v.size s
        WHERE o.customer.id = :userId
        ORDER BY o.orderDate DESC
    """)
    List<Order> findByCustomerIdWithDetails(@Param("userId") Integer userId);
}

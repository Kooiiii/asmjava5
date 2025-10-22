package com.poly.asm.dao;

import com.poly.asm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCustomerId(Integer customerId);
    List<Order> findByStatus(String status);
    List<Order> findByOrderDateBetween(Date startDate, Date endDate);
    void deleteById(Integer id);
}
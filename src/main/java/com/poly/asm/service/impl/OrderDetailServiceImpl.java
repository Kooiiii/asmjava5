package com.poly.asm.service.impl;

import com.poly.asm.dao.OrderDetailRepository;
import com.poly.asm.entity.Order;
import com.poly.asm.entity.OrderDetail;
import com.poly.asm.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail save(OrderDetail detail) {
        return orderDetailRepository.save(detail);
    }

    @Override
    public List<OrderDetail> findByOrder(Order order) {
        return orderDetailRepository.findByOrderId(order.getId());
    }

    @Override
    public List<OrderDetail> findByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public void deleteByOrderId(Integer orderId) {
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        if (!details.isEmpty()) {
            orderDetailRepository.deleteAll(details);
        }
    }
}

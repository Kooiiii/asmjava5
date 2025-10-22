package com.poly.asm.service.impl;

import com.poly.asm.dao.*;
import com.poly.asm.entity.*;
import com.poly.asm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductVariantRepository productVariantRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AddressRepository addressRepository;

    @Override
    public Order createOrder(Integer userId, Integer addressId) {
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại!"));

        Order order = new Order();
        order.setCustomer(user);
        order.setShippingAddress(address);
        order.setOrderDate(new Date());
        order.setStatus("Chờ duyệt");
        order.setPaymentMethod("COD");

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getProductVariant();
            if (variant.getQuantity() < cartItem.getQuantity()) {

                throw new RuntimeException("Sản phẩm '" + variant.getProduct().getName() + "' (Size: " + variant.getSize().getName() + ", Màu: "+ variant.getColor().getName() +") không đủ hàng!");
            }
            variant.setQuantity(variant.getQuantity() - cartItem.getQuantity());
            productVariantRepository.save(variant);

            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProductVariant(variant);
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(variant.getPrice());
            orderDetailRepository.save(detail);
        }
        cartItemRepository.deleteByCustomerId(userId);

        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUserId(Integer userId) {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = findById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
    }

    @Override
    public void deleteById(Integer id) {
        Order order = findById(id);
        List<OrderDetail> details = orderDetailRepository.findByOrderId(id);
        orderDetailRepository.deleteAll(details);
        orderRepository.delete(order);
    }
}
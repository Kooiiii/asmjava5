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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    // Đảm bảo tất cả các thao tác DB thành công hoặc rollback
    @Transactional
    public Order createOrder(Integer userId, Integer addressId) {
        // Lấy thông tin giỏ hàng của user
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại!"));

        // Tạo một đối tượng Order mới
        Order order = new Order();
        order.setCustomer(user);
        order.setShippingAddress(address);
        order.setOrderDate(new Date());
        order.setStatus("Chờ duyệt");
        // Thanh Toán Bằng COD luôn là Mặc định
        order.setPaymentMethod("COD");

        // Lưu Order để lấy ID
        Order savedOrder = orderRepository.save(order);

        // Chuyển các CartItem thành OrderDetail và trừ kho
        for (CartItem cartItem : cartItems) {
            ProductVariant variant = cartItem.getProductVariant();

            // Kiểm tra tồn kho
            if (variant.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + variant.getProduct().getName() + " không đủ hàng!");
            }

            // Trừ số lượng tồn kho
            variant.setQuantity(variant.getQuantity() - cartItem.getQuantity());
            productVariantRepository.save(variant);

            // Tạo OrderDetail
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProductVariant(variant);
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(variant.getPrice());
            orderDetailRepository.save(detail);
        }

        // Xóa giỏ hàng của user
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
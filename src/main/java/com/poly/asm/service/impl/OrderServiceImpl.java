package com.poly.asm.service.impl;

import com.poly.asm.dao.*;
import com.poly.asm.entity.*;
import com.poly.asm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductVariantRepository productVariantRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AddressRepository addressRepository;

    // 🟩 Tạo đơn hàng mới
    @Override
    public Order createOrder(Integer userId, Integer addressId) {
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại!"));

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
                throw new RuntimeException("Sản phẩm '" + variant.getProduct().getName()
                        + "' (Size: " + variant.getSize().getName()
                        + ", Màu: " + variant.getColor().getName() + ") không đủ hàng!");
            }

            // Trừ kho
            variant.setQuantity(variant.getQuantity() - cartItem.getQuantity());
            productVariantRepository.save(variant);

            // Tạo chi tiết đơn hàng
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProductVariant(variant);
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(variant.getPrice());
            orderDetailRepository.save(detail);
        }

        // Xóa giỏ hàng sau khi đặt
        cartItemRepository.deleteByCustomerId(userId);

        return savedOrder;
    }

    // 🟩 Lấy danh sách đơn hàng của 1 user (đã fetch chi tiết)
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUserId(Integer userId) {
        return orderRepository.findByCustomerIdWithDetails(userId);
    }

    // 🟩 Lấy tất cả đơn hàng (admin)
    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    // 🟩 Cập nhật trạng thái
    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = findById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // 🟩 Tìm theo ID
    @Override
    @Transactional(readOnly = true)
    public Order findById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
    }

    // 🟩 Xóa đơn hàng (và các chi tiết liên quan)
    @Override
    public void deleteById(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
        orderDetailRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
    }

    // 🟩 Tìm đơn hàng theo ID và User (dùng trong /order/detail/{id})
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findByIdAndUserId(Integer orderId, Integer userId) {
        return orderRepository.findByIdAndCustomerIdWithDetails(orderId, userId);
    }

    // 🟩 Tính tổng giá trị đơn hàng
    @Override
    public BigDecimal calculateTotal(Order order) {
        if (order == null || order.getOrderDetails() == null) {
            return BigDecimal.ZERO;
        }

        return order.getOrderDetails()
                .stream()
                .map(detail -> detail.getTotal() == null ? BigDecimal.ZERO : detail.getTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

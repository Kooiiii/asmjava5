package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.*;
import com.poly.asm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressService addressService;

    @PostMapping("/checkout")
    public String checkout(Model model) {
        // 1️⃣ Lấy user hiện tại
        User customer = userSession.getCurrentUser();
        if (customer == null) {
            return "redirect:/login";
        }

        Integer userId = customer.getId();

        // 2️⃣ Lấy danh sách sản phẩm trong giỏ
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng trống!");
            return "cart";
        }

        // 3️⃣ Lấy địa chỉ giao hàng (mặc định)
        List<Address> addresses = addressService.findByUserId(userId);
        if (addresses.isEmpty()) {
            model.addAttribute("error", "Vui lòng thêm địa chỉ giao hàng trước khi thanh toán!");
            return "cart";
        }
        Address address = addresses.stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElse(addresses.get(0));

        // 4️⃣ Tạo đơn hàng
        Order order = orderService.createOrder(userId, address.getId());

        // 5️⃣ Lấy danh sách chi tiết đơn hàng
        List<OrderDetail> orderDetails = orderDetailService.findByOrder(order);

        // 6️⃣ Xóa giỏ hàng sau khi thanh toán
        cartService.clearCart(userId);

        // 7️⃣ Truyền dữ liệu ra view invoice.html
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("customerName", customer.getUsername());
        model.addAttribute("customerEmail", customer.getEmail());
        model.addAttribute("customerPhone", customer.getPhone());
        model.addAttribute("shippingAddress", address.getAddress());
        model.addAttribute("paymentMethod", order.getPaymentMethod());
        model.addAttribute("status", order.getStatus());

        return "invoice";
    }
}

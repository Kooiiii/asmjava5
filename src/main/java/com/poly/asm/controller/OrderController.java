package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Order;
import com.poly.asm.entity.OrderDetail;
import com.poly.asm.entity.User;
import com.poly.asm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserSession userSession;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        // Thêm logic lấy giỏ hàng và địa chỉ giao hàng
        return "checkout";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam Integer addressId, Model model) {
        try {
            User currentUser = userSession.getCurrentUser();
            if (currentUser == null) {
                return "redirect:/auth/login";
            }

            Order order = orderService.createOrder(currentUser.getId(), addressId);
            model.addAttribute("order", order);
            return "redirect:/order/success/" + order.getId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "checkout";
        }
    }

    @GetMapping("/success/{orderId}")
    public String orderSuccess(@PathVariable Integer orderId, Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        // Có thể lấy thông tin order từ service nếu cần
        model.addAttribute("orderId", orderId);
        return "checkout_success";
    }

    @GetMapping("/list")
    public String orderList(Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        List<Order> orders = orderService.findByUserId(currentUser.getId());
        model.addAttribute("orders", orders);
        model.addAttribute("user", currentUser);
        return "orders";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable Integer orderId, Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Optional<Order> optionalOrder = orderService.findByIdAndUserId(orderId, currentUser.getId());
        if (optionalOrder.isEmpty()) {
            return "redirect:/order/list";
        }

        Order order = optionalOrder.get();

        System.out.println("➡️ Order ID: " + order.getId());
        System.out.println("➡️ OrderDetails size: " +
                (order.getOrderDetails() == null ? "null" : order.getOrderDetails().size()));

        BigDecimal totalAmount = orderService.calculateTotal(order);
        System.out.println("➡️ Total Amount: " + totalAmount);

        model.addAttribute("order", order);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("user", currentUser);

        return "order_detail";
    }

}
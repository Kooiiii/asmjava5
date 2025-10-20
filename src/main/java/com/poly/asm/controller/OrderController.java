package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Order;
import com.poly.asm.entity.User;
import com.poly.asm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        // Cần implement service để lấy order detail
        List<Order> userOrders = orderService.findByUserId(currentUser.getId());
        Order order = userOrders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);

        if (order == null) {
            return "redirect:/order/list";
        }

        model.addAttribute("order", order);
        model.addAttribute("user", currentUser);
        return "order_detail";
    }
}
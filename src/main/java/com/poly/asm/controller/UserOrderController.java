package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Order;
import com.poly.asm.entity.User;
import com.poly.asm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderService orderService;
    private final UserSession userSession;

    // ✅ Hiển thị danh sách đơn hàng của người dùng hiện tại
    @GetMapping
    public String myOrders(Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        var orders = orderService.findByUserId(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("orders", orders);
        return "userorder-list";
    }

    // ✅ Hiển thị chi tiết đơn hàng cụ thể
    @GetMapping("/detail/{orderId}")
    public String viewOrderDetail(@PathVariable("orderId") Integer orderId, Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        var orderOpt = orderService.findByIdAndUserId(orderId, currentUser.getId());
        if (orderOpt.isEmpty()) {
            return "redirect:/orders";
        }

        Order order = orderOpt.get();
        var total = orderService.calculateTotal(order);

        model.addAttribute("user", currentUser);
        model.addAttribute("order", order);
        model.addAttribute("total", total);
        return "userorder-detail";
    }
}

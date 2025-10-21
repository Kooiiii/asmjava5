package com.poly.asm.controller.Admin;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Order;
import com.poly.asm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderAController {

    private final OrderService orderService;
    private final UserSession userSession;

    @GetMapping
    public String orderManagement(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("user", userSession.getCurrentUser());
        return "admin/orders";
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer orderId,
                                    @RequestParam String status,
                                    Model model) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            model.addAttribute("message", "Cập nhật trạng thái thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable Integer orderId, Model model) {
        // Logic xem chi tiết đơn hàng cho admin
        return "admin/order_detail";
    }
}
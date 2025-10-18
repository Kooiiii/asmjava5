//package com.poly.asm.controller;
//
//import com.poly.asm.config.UserSession;
//import com.poly.asm.entity.Order;
//import com.poly.asm.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import java.util.Arrays;
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin/orders")
//@RequiredArgsConstructor
//public class OrderAController {
//
//    private final UserSession userSession;
//
//    @GetMapping
//    public String orderManagement(Model model) {
//        model.addAttribute("user", userSession.getCurrentUser());
//
//        List<Order> orders = Arrays.asList(
//                new Order(1L, new User(1L, "Nguyễn Văn A"), 950000.0, "Đã giao"),
//                new Order(2L, new User(2L, "Trần Thị B"), 1200000.0, "Đang xử lý"),
//                new Order(3L, new User(1L, "Nguyễn Văn A"), 550000.0, "Đang giao")
//        );
//
//        model.addAttribute("orders", orders);
//        return "orders";
//    }
//
//    @GetMapping("/{id}")
//    public String orderDetail(@PathVariable String id, Model model) {
//        model.addAttribute("user", userSession.getCurrentUser());
//        return "order_detail";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteOrder(@PathVariable String id, Model model) {
//        return "redirect:/admin/orders";
//    }
//}
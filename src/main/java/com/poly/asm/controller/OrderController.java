package com.poly.asm.controller;

import com.poly.asm.entity.CartItem;
import com.poly.asm.entity.Order;
import com.poly.asm.entity.Product;
import com.poly.asm.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class OrderController {

    @GetMapping("/order/checkout")
    public String checkout(Model model){
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1L, new Product(1L, "Áo thun nam", 250000.0, "https://source.unsplash.com/300x300/?tshirt"), 2),
                new CartItem(2L, new Product(2L, "Quần jeans", 450000.0, "https://source.unsplash.com/300x300/?jeans"), 1)
        );

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "checkout";
    }

    @GetMapping("/order/list")
    public String list(Model model){
        List<Order> orders = Arrays.asList(
                new Order(1L, new User(1L, "Nguyễn Văn A"), 950000.0, "Đã giao"),
                new Order(2L, new User(1L, "Nguyễn Văn A"), 1200000.0, "Đang xử lý"),
                new Order(3L, new User(1L, "Nguyễn Văn A"), 550000.0, "Đang giao")
        );

        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model){
        return "checkout";
    }

    @GetMapping("/checkout/process")
    public String processCheckout(Model model){
        return "redirect:/checkout/success";
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess(Model model){
        return "checkout_success";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable String id, Model model){
        Order order = new Order(
                Long.parseLong(id),
                new User(1L, "Nguyễn Văn A"),
                950000.0,
                "Đã giao"
        );
        model.addAttribute("order", order);
        return "order_detail";
    }
}
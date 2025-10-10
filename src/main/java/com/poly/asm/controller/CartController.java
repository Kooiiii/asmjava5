package com.poly.asm.controller;

import com.poly.asm.entity.CartItem;
import com.poly.asm.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    // Giả lập danh sách sản phẩm (thay bằng ProductService nếu có)
    private static final Map<Long, Product> productDB = new HashMap<>();
    static {
        productDB.put(1L, new Product(1L, "Áo thun nam", "Áo thun cotton", 250000.0, "https://source.unsplash.com/300x300/?tshirt"));
        productDB.put(2L, new Product(2L, "Quần jeans", "Quần jeans form đẹp", 450000.0, "https://source.unsplash.com/300x300/?jeans"));
        productDB.put(3L, new Product(3L, "Giày sneaker", "Sneaker năng động", 600000.0, "https://source.unsplash.com/300x300/?sneaker"));
    }

    // Lấy giỏ hàng từ session
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("CART");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("CART", cart);
        }
        return cart;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Long, CartItem> cart = getCart(session);
        double totalPrice = cart.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @GetMapping("/add/{id}/{quantity}")
    public String addCart(@PathVariable Long id,
                          @PathVariable int quantity,
                          HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);

        Product product = productDB.get(id);
        if (product != null) {
            CartItem item = cart.get(id);
            if (item == null) {
                item = new CartItem(System.currentTimeMillis(), product, quantity);
            } else {
                item.setQuantity(item.getQuantity() + quantity);
            }
            cart.put(id, item);
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeCart(@PathVariable Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(id);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("CART");
        return "redirect:/cart";
    }
}

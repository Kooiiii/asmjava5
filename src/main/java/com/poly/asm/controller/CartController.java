package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.User;
import com.poly.asm.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserSession userSession;

    @GetMapping
    public String viewCart(Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        // Truyền user object đến template
        model.addAttribute("user", currentUser);
        model.addAttribute("cartItems", cartService.getCartItems(currentUser.getId()));

        // Tính tổng tiền
        double totalPrice = cartService.getCartItems(currentUser.getId()).stream()
                .mapToDouble(item -> item.getProductVariant().getPrice().doubleValue() * item.getQuantity())
                .sum();
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    @PostMapping("/add")
    public String addCart(@RequestParam("variantId") Integer variantId,
                          @RequestParam("quantity") int quantity) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        try {
            cartService.addToCart(currentUser.getId(), variantId, quantity);
        } catch (Exception e) {
            return "redirect:/products/" + variantId + "?error=" + e.getMessage();
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeCart(@PathVariable("id") Integer cartItemId) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        try {
            cartService.removeCartItem(cartItemId, currentUser.getId());
        } catch (Exception e) {
            return "redirect:/cart?error=" + e.getMessage();
        }
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        cartService.clearCart(currentUser.getId());
        return "redirect:/cart";
    }
}
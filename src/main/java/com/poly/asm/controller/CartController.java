package com.poly.asm.controller;

import com.poly.asm.entity.User;
import com.poly.asm.service.CartService;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;

    // Helper method để lấy User ID
    private Integer getUserId(Principal principal) {
        if (principal == null) return null;
        User user = userService.findByUsername(principal.getName()).orElse(null);
        return (user != null) ? user.getId() : null;
    }

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        Integer userId = getUserId(principal);
        if (userId == null) return "redirect:/auth/login";

        model.addAttribute("cartItems", cartService.getCartItems(userId));
        // Cần tính tổng tiền trong service hoặc tại đây
        double totalPrice = cartService.getCartItems(userId).stream()
                .mapToDouble(item -> item.getProductVariant().getPrice().doubleValue() * item.getQuantity())
                .sum();
        model.addAttribute("totalPrice", totalPrice);

        return "cart"; // Trả về cart.html
    }

    // Đường dẫn trong index.html là /cart/add/{id}/{quantity}
    // Cần sửa lại để linh hoạt hơn
    @PostMapping("/add")
    public String addCart(@RequestParam("variantId") Integer variantId,
                          @RequestParam("quantity") int quantity,
                          Principal principal) {
        Integer userId = getUserId(principal);
        if (userId == null) return "redirect:/auth/login";

        try {
            cartService.addToCart(userId, variantId, quantity);
        } catch (Exception e) {
            // Xử lý lỗi (ví dụ: hết hàng)
            return "redirect:/products/" + variantId + "?error=" + e.getMessage();
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeCart(@PathVariable("id") Integer cartItemId, Principal principal) {
        // Cần kiểm tra cartItemId này có thuộc user đang đăng nhập không
        cartService.removeCartItem(cartItemId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(Principal principal) {
        Integer userId = getUserId(principal);
        if (userId == null) return "redirect:/auth/login";

        cartService.clearCart(userId);
        return "redirect:/cart";
    }
}
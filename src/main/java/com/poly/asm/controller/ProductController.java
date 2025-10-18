package com.poly.asm.controller;

import com.poly.asm.entity.Product;
import com.poly.asm.service.ProductService;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private UserService userService;

    // Helper method
    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            userService.findByUsername(principal.getName()).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
    }

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        addUserToModel(model, principal);

        model.addAttribute("products", productService.findAll());
        // Logic phân trang có thể thêm sau
        // model.addAttribute("currentPage", 0);
        // model.addAttribute("totalPages", 1);
        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model, Principal principal) {
        addUserToModel(model, principal);

        try {
            Product product = productService.findById(id);
            model.addAttribute("product", product);
            return "product_detail";
        } catch (Exception e) {
            return "redirect:/products?error=notfound";
        }
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String keyword, Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("keyword", keyword);
        return "product-list"; // Trả về product-list.html
    }
}
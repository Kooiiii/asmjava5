package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Product;
import com.poly.asm.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserSession userSession;


    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userSession.getCurrentUser());

        try {
            Product product = productService.findById(id);
            if (product.getVariants().isEmpty()) {
                return "redirect:/products?error=novariants";
            }
            model.addAttribute("product", product);
            return "product_detail";
        } catch (Exception e) {
            return "redirect:/products?error=notfound";
        }
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        // Thêm user vào model giống CartController
        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("keyword", keyword);
        return "product-list";
    }
}
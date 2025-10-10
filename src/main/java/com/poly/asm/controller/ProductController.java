package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final UserSession userSession;

    @GetMapping("/products")
    public String products(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("user", userSession.getCurrentUser());

        List<Product> products = Arrays.asList(
                new Product(1L, "Áo thun nam", "Áo thun cotton thoáng mát", 250000.0, 50, "/photos/ao1.jpg"),
                new Product(2L, "Quần jeans", "Quần jeans form đẹp", 450000.0, 30, "/photos/quan.jpg"),
                new Product(3L, "Áo khoác", "Áo khoác thời trang", 1200000.0, 20, "/photos/aokhoac.jpg")
        );

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 3);
        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        model.addAttribute("user", userSession.getCurrentUser());

        String imagePath = switch (id) {
            case "1" -> "/photos/ao1.jpg";
            case "2" -> "/photos/quan.jpg";
            case "3" -> "/photos/aokhoac.jpg";
            default -> "/photos/ao1.jpg";
        };

        Product product = new Product(
                Long.parseLong(id),
                "Sản phẩm " + id,
                "Mô tả chi tiết sản phẩm " + id,
                250000.0,
                50,
                imagePath
        );

        model.addAttribute("product", product);
        return "product_detail";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("product", new Product());
        return "add_product";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable String id, Model model) {
        model.addAttribute("user", userSession.getCurrentUser());

        String imagePath = switch (id) {
            case "1" -> "/photos/ao1.jpg";
            case "2" -> "/photos/quan.jpg";
            case "3" -> "/photos/aokhoac.jpg";
            default -> "/photos/ao1.jpg";
        };

        Product product = new Product(
                Long.parseLong(id),
                "Áo thun nam " + id,
                "Mô tả sản phẩm " + id,
                250000.0,
                50,
                imagePath
        );

        model.addAttribute("product", product);
        return "edit_product";
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        model.addAttribute("user", userSession.getCurrentUser());

        List<Product> products = Arrays.asList(
                new Product(1L, "Áo thun nam " + keyword, "Sản phẩm tìm kiếm: " + keyword, 250000.0, 50, "/photos/ao1.jpg"),
                new Product(2L, "Quần " + keyword, "Sản phẩm tìm kiếm: " + keyword, 450000.0, 30, "/photos/quan.jpg")
        );

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "product-list";
    }
}

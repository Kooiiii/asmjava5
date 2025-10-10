package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAController {

    private final UserSession userSession;

    @GetMapping
    public String productManagement(Model model) {
        // Gửi thông tin user hiện tại cho view
        model.addAttribute("user", userSession.getCurrentUser());

        // Danh sách sản phẩm demo
        List<Product> products = Arrays.asList(
                new Product(1L, "Áo thun nam", "Áo thun cotton thoáng mát", 250000.0, 50, "/photos/ao1.jpg"),
                new Product(2L, "Quần jeans", "Quần jeans form đẹp", 450000.0, 30, "/photos/quan.jpg"),
                new Product(3L, "Áo khoác", "Áo khoác thời trang", 1200000.0, 20, "/photos/aokhoac.jpg")
        );

        // Gửi dữ liệu sang view
        model.addAttribute("products", products);
        return "products"; // tên file template: templates/products.html
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        // Ở đây bạn có thể thêm logic xóa thật từ database
        // Ví dụ: productService.deleteById(id);
        System.out.println("Xóa sản phẩm có ID = " + id);

        // Sau khi xóa, quay lại trang danh sách
        return "redirect:/admin/products";
    }
}

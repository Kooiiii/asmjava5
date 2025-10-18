//package com.poly.asm.controller;
//
//import com.poly.asm.config.UserSession;
//import com.poly.asm.entity.Product;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import java.util.Arrays;
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class HomeController {
//
//    private final UserSession userSession;
//
//    @GetMapping("/")
//    public String home(Model model) {
//        model.addAttribute("user", userSession.getCurrentUser());
//
//        // ⚙️ Sửa đường dẫn ảnh thành ảnh nội bộ trong /static/images/
//        List<Product> featuredProducts = Arrays.asList(
//                new Product(1L, "Áo thun nam cao cấp", "Áo thun chất liệu cotton 100%", 250000.0, "/photos/ao1.jpg"),
//                new Product(2L, "Quần jeans nữ", "Quần jeans form slim đẹp", 450000.0, "/photos/quan.jpg"),
//                new Product(3L, "Áo khoác da", "Áo khoác da thật phong cách", 1200000.0, "/photos/aokhoac.jpg")
//        );
//
//        model.addAttribute("featuredProducts", featuredProducts);
//        return "index";
//    }
//
//    @GetMapping("/home")
//    public String homePage(Model model) {
//        return home(model);
//    }
//}

package com.poly.asm.controller;

import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MyController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {

        if (principal == null) {
            // Người dùng chưa đăng nhập, chuyển về trang login
            return "redirect:/auth/login";
        }

        String username = principal.getName();

        // Dùng username để lấy đầy đủ thông tin User từ Service
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", currentUser);

        return "profile";
    }
}
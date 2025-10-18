package com.poly.asm.controller;

import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/login")
    public String login() {
        return "login"; // Trả về login.html
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot_password"; // Trả về forgot_password.html
    }

    @GetMapping("/sign-up")
    public String register(Model model) {
        model.addAttribute("user", new User()); // Dùng thẳng Entity User
        return "register"; // Trả về register.html
    }
    // Khớp với th:action trong register.html
    @PostMapping("/register/saveUser")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            // Service sẽ mã hóa mật khẩu và gán role
            userService.registerUser(user);
            return "redirect:/auth/login?register_success";
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
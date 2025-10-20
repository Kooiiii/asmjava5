package com.poly.asm.controller;

import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "register_success", required = false) String registerSuccess,
            Model model) {

        // Xử lý thông báo lỗi đăng nhập
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }

        // Xử lý thông báo đăng xuất
        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất thành công!");
        }

        // Xử lý thông báo đăng ký thành công
        if (registerSuccess != null) {
            model.addAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
        }

        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot_password";
    }

    @GetMapping("/sign-up")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register/saveUser")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            // Kiểm tra mật khẩu xác nhận (nếu có)
            if (user.getPassword() == null || user.getPassword().length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                return "register";
            }

            // Đảm bảo role mặc định
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("ROLE_CUSTOMER");
            }

            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/auth/login?register_success";

        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // Xử lý quên mật khẩu
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email,
                                        Model model) {
        try {
            // TODO: Triển khai logic gửi email reset mật khẩu
            model.addAttribute("message", "Hướng dẫn reset mật khẩu đã được gửi đến email: " + email);
        } catch (Exception e) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
        }
        return "forgot_password";
    }
}
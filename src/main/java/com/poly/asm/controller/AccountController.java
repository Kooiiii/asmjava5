package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserSession userSession;
    private final UserService userService;

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
        return "update_profile";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
        return "profile";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
        return "change_password";
    }

    // ✅ POST - xử lý đổi mật khẩu
    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model
    ) {
        User currentUser = userSession.getCurrentUser();
        model.addAttribute("user", currentUser);

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin!");
            return "change_password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            return "change_password";
        }

        // ✅ Kiểm tra độ dài tối thiểu
        if (newPassword.length() < 8) {
            model.addAttribute("errorMessage", "Mật khẩu mới phải có ít nhất 8 ký tự!");
            return "change_password";
        }

        try {
            boolean success = userService.changePassword(currentUser, oldPassword, newPassword);
            if (success) {
                model.addAttribute("successMessage", "Đổi mật khẩu thành công!");
            } else {
                model.addAttribute("errorMessage", "Không thể đổi mật khẩu, vui lòng thử lại!");
            }
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "change_password";
    }
}

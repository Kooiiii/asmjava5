package com.poly.asm.controller;

import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import com.poly.asm.config.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSession userSession;

    // 👉 Hiển thị form cập nhật hồ sơ
    @GetMapping("/update")
    public String showProfileForm(Model model) {
        User currentUser = userSession.getCurrentUser();

        if (currentUser == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để xem thông tin cá nhân!");
            return "login"; // Chuyển hướng về trang đăng nhập
        }

        model.addAttribute("user", currentUser);
        return "profile_update"; // Giao diện: templates/profile_update.html
    }

    // 👉 Xử lý cập nhật hồ sơ
    @PostMapping("/update")
    public String updateProfile(User user, Model model) {
        User currentUser = userSession.getCurrentUser();

        if (currentUser == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để cập nhật thông tin!");
            return "login";
        }

        // Giữ lại thông tin quan trọng không được phép thay đổi
        user.setId(currentUser.getId());
        user.setPassword(currentUser.getPassword());
        user.setUsername(currentUser.getUsername());

        // Cập nhật
        User updatedUser = userService.updateProfile(user);

        // Cập nhật lại session
        userSession.setCurrentUser(updatedUser);

        model.addAttribute("message", "Cập nhật hồ sơ thành công!");
        model.addAttribute("user", updatedUser);

        return "profile";
    }
}

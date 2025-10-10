package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAController {

    private final UserSession userSession;

    @GetMapping("/users")
    public String userManagement(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());

        List<User> users = Arrays.asList(
                new User(1L, "user1", "Nguyễn Văn A", "user1@email.com", "0123456781", "Hà Nội", "USER"),
                new User(2L, "user2", "Trần Thị B", "user2@email.com", "0123456782", "TP.HCM", "USER"),
                new User(3L, "admin", "Admin System", "admin@email.com", "0123456789", "Hà Nội", "ADMIN")
        );

        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable String id, Model model) {
        model.addAttribute("user", userSession.getCurrentUser());

        User userDetail = new User(
                Long.parseLong(id),
                "user" + id,
                "Nguyễn Văn " + (char)('A' + Integer.parseInt(id) - 1),
                "user" + id + "@email.com",
                "012345678" + id,
                "Địa chỉ " + id,
                "USER"
        );

        model.addAttribute("userDetail", userDetail);
        return "user_detail";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable String id, Model model) {
        return "redirect:/admin/users";
    }
}
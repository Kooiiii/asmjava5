package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserSession userSession;

    @GetMapping("/profile/edit")
    public String editProfile(Model model){
        model.addAttribute("user", userSession.getCurrentUser());
        return "update_profile";
    }

    @GetMapping("/profile")
    public String profile(Model model){
        model.addAttribute("user", userSession.getCurrentUser());
        return "profile";
    }

    @GetMapping("/profile/change-password")
    public String changePassword(Model model){
        model.addAttribute("user", userSession.getCurrentUser());
        return "change_password";
    }
}
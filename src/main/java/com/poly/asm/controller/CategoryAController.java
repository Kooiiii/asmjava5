package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAController {

    private final UserSession userSession;

    @GetMapping
    public String categoryManagement(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
        return "categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable String id, Model model) {
        return "redirect:/admin/categories";
    }
}
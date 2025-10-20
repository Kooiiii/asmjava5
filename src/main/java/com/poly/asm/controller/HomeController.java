package com.poly.asm.controller;

import com.poly.asm.entity.Product;
import com.poly.asm.entity.User;
import com.poly.asm.service.ProductService;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired private ProductService productService;
    @Autowired private UserService userService;

    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userService.findByUsername(username).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        addUserToModel(model, principal);

        List<Product> featuredProducts = productService.findAll();
        model.addAttribute("featuredProducts", featuredProducts != null ? featuredProducts : new ArrayList<>());

        return "index";
    }

    @GetMapping("/home")
    public String homePage(Model model, Principal principal) {
        return home(model, principal);
    }
}
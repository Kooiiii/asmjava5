package com.poly.asm.config;

import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    public GlobalControllerAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addLoggedUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            userRepository.findByUsername(auth.getName())
                    .ifPresentOrElse(
                            user -> model.addAttribute("user", user),
                            () -> model.addAttribute("user", null)
                    );
        } else {
            model.addAttribute("user", null);
        }
    }
}

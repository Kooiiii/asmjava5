package com.poly.asm.config;

import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    @Autowired
    private UserService userService;

    public User getCurrentUser() {
        String username = getCurrentUsername();
        if (username != null) {
            return userService.findByUsername(username).orElse(null);
        }
        return null;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {
            return authentication.getName();
        }
        return null;
    }
}
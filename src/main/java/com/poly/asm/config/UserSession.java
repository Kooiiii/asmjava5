package com.poly.asm.config;

import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {

    @Autowired
    private UserService userService;

    // ✅ Cập nhật user trong session
    @Setter
    private User currentUser; // ✅ Biến lưu user trong session

    public User getCurrentUser() {
        if (currentUser != null) {
            return currentUser;
        }

        String username = getCurrentUsername();
        if (username != null) {
            currentUser = userService.findByUsername(username).orElse(null);
        }
        return currentUser;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {
            return authentication.getName();
        }
        return null;
    }

    public void clear() {
        this.currentUser = null;
        SecurityContextHolder.clearContext();
    }
}

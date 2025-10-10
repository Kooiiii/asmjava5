package com.poly.asm.config;

import com.poly.asm.entity.User;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Data
public class UserSession {
    private User currentUser;

    public UserSession() {
        this.currentUser = createDefaultUser();
    }

    private User createDefaultUser() {
        // Đổi role ở đây để test: "USER" hoặc "ADMIN"
        return new User(1L, "user123", "Nguyễn Văn A", "user@email.com",
                "0123456789", "Hà Nội", "USER");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }

    public boolean isUser() {
        return currentUser != null && "USER".equals(currentUser.getRole());
    }
}
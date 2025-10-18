package com.poly.asm.controller;
import java.security.Principal;

@Controller
public class MyController {

    @Autowired
    private UserService userService; // Bạn cần UserService để lấy full thông tin

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {

        if (principal == null) {
            // Người dùng chưa đăng nhập, chuyển về trang login
            return "redirect:/auth/login";
        }

        // Lấy username từ Principal
        String username = principal.getName();

        // Dùng username để lấy đầy đủ thông tin User từ Service
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Giờ bạn có thể thêm user vào model
        model.addAttribute("user", currentUser);

        return "profile";
    }
}
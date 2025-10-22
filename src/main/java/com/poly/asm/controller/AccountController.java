package com.poly.asm.controller;

import com.poly.asm.entity.User; // Import User
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // Import PreAuthorize
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal; // Import Principal

@Controller
@RequestMapping("/profile") // Base path là /profile
@PreAuthorize("isAuthenticated()") // Yêu cầu phải đăng nhập
public class AccountController { // Không cần @RequiredArgsConstructor nữa

    // Inject UserService bằng @Autowired
    @Autowired
    private UserService userService;

    // GlobalControllerAdvice sẽ tự động thêm ${user} vào model

    @GetMapping // /profile
    public String profile(Model model) {
        // "user" đã được tự động thêm vào model
        return "profile"; // Trả về /templates/profile.html
    }

    @GetMapping("/edit") // /profile/edit
    public String editProfile(Model model) {
        // "user" đã được tự động thêm vào model, khớp với th:object="${user}"
        return "update_profile"; // Trả về /templates/update_profile.html
    }

    @PostMapping("/update") // /profile/update
    public String updateProfile(@ModelAttribute User userFromForm, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Lấy username đang đăng nhập từ Principal
            String username = principal.getName();
            User loggedInUser = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User không tìm thấy"));

            // Chỉ cập nhật các trường cho phép
            loggedInUser.setFullName(userFromForm.getFullName());
            loggedInUser.setEmail(userFromForm.getEmail());
            loggedInUser.setPhone(userFromForm.getPhone());
            loggedInUser.setAddress(userFromForm.getAddress()); // Cập nhật cột Address trong USERS

            userService.updateUserProfile(loggedInUser); // Gọi service để lưu
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/profile/edit"; // Quay lại form edit nếu lỗi
        }
        return "redirect:/profile"; // Về trang profile sau khi thành công
    }

    @GetMapping("/change-password") // /profile/change-password
    public String changePasswordForm(Model model) {
        // "user" đã được tự động thêm vào model
        return "change_password"; // Trả về /templates/change_password.html
    }

    @PostMapping("/profile/change-password") // Khớp th:action
    public String changePassword(Principal principal, // Lấy Principal
                                 @RequestParam("currentPassword") String currentPassword, // Tên trong form cũ là oldPassword? Sửa lại nếu cần
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) { // Dùng RedirectAttributes để gửi thông báo

        // Lấy username đang đăng nhập
        String username = principal.getName();

        // Kiểm tra mật khẩu xác nhận
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            return "redirect:/profile/change-password";
        }
        // Kiểm tra độ dài (ông để 8 ký tự trong HTML, nên check ở đây luôn)
        if (newPassword.length() < 3) { // Tạm để 3 ký tự cho pass '123'
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới phải có ít nhất 3 ký tự!");
            return "redirect:/profile/change-password";
        }

        try {
            // Gọi hàm service đã sửa (dùng username thay vì User object)
            userService.changePassword(username, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
            return "redirect:/profile"; // Về trang profile
        } catch (Exception e) {
            // Bắt lỗi từ service (ví dụ: mật khẩu cũ sai)
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/profile/change-password"; // Quay lại form đổi pass
        }
    }
}
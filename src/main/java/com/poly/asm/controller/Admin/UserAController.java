package com.poly.asm.controller.Admin;

import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
public class UserAController {

    @Autowired
    private UserRepository userRepository;

    // 📋 DANH SÁCH NGƯỜI DÙNG
    @GetMapping
    public String listUsers(Model model,
                            @ModelAttribute("success") String successMsg,
                            @ModelAttribute("error") String errorMsg) {

        List<User> users = userRepository.findAll();

        // Ẩn mật khẩu khi hiển thị
        List<User> safeUsers = users.stream().map(u -> {
            User copy = new User();
            copy.setId(u.getId());
            copy.setUsername(u.getUsername());
            copy.setPassword("***");
            copy.setRole(u.getRole());
            copy.setEmail(u.getEmail());
            copy.setPhone(u.getPhone());
            copy.setFullName(u.getFullName());
            copy.setBirthday(u.getBirthday());
            copy.setGender(u.getGender());
            copy.setAddress(u.getAddress());
            return copy;
        }).collect(Collectors.toList());

        model.addAttribute("users", safeUsers);
        model.addAttribute("success", successMsg);
        model.addAttribute("error", errorMsg);
        return "admin/user_list"; // ✅ templates/admin/user_list.html
    }

    // 🔍 XEM CHI TIẾT NGƯỜI DÙNG (Chỉ xem, không chỉnh sửa)
    @GetMapping("/{id}")
    public String userDetail(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirect.addFlashAttribute("error", "Không tìm thấy người dùng!");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/user_detail"; // ✅ templates/admin/user_detail.html
    }

    // ❌ XÓA NGƯỜI DÙNG
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        try {
            userRepository.deleteById(id);
            redirect.addFlashAttribute("alertMessage", "Đã xóa người dùng thành công!");
        } catch (DataIntegrityViolationException e) {
            redirect.addFlashAttribute("alertMessage", "Không thể xóa người dùng — có dữ liệu ràng buộc (giỏ hàng, đơn hàng, v.v.)!");
        } catch (Exception e) {
            redirect.addFlashAttribute("alertMessage", "Đã xảy ra lỗi khi xóa người dùng!");
        }
        return "redirect:/admin/users";
    }
}

package com.poly.asm.controller.Admin;

import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.User;
import jakarta.transaction.Transactional;
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

    // 📋 DANH SÁCH NGƯỜI DÙNG + LỌC THEO VAI TRÒ
    @GetMapping
    public String listUsers(@RequestParam(value = "role", required = false) String role,
                            Model model,
                            @ModelAttribute("success") String successMsg,
                            @ModelAttribute("error") String errorMsg) {

        List<User> users;

        // 🔍 Nếu có chọn vai trò thì lọc, còn không thì lấy tất cả
        if (role != null && !role.isEmpty()) {
            users = userRepository.findAll()
                    .stream()
                    .filter(u -> u.getRole() != null && u.getRole().equalsIgnoreCase(role))
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findAll();
        }

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
        model.addAttribute("selectedRole", role); // ✅ để giữ lựa chọn lọc
        model.addAttribute("success", successMsg);
        model.addAttribute("error", errorMsg);
        return "admin/user_list";
    }

    // 🔍 XEM CHI TIẾT NGƯỜI DÙNG
    @GetMapping("/{id}")
    public String userDetail(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirect.addFlashAttribute("error", "Không tìm thấy người dùng!");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/user_detail";
    }

    // ❌ XÓA NGƯỜI DÙNG
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            redirect.addFlashAttribute("alertMessage", "Không tìm thấy người dùng!");
            return "redirect:/admin/users";
        }

        // 🚫 Không cho xóa tài khoản Admin
        if ("Admin".equalsIgnoreCase(user.getRole())) {
            redirect.addFlashAttribute("alertMessage", "Không thể xóa tài khoản Admin!");
            return "redirect:/admin/users";
        }

        try {
            userRepository.delete(user);
            redirect.addFlashAttribute("alertMessage", "Đã xóa người dùng thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("alertMessage", "Không thể xóa người dùng — có dữ liệu ràng buộc!");
        }

        return "redirect:/admin/users";
    }

    // ➕ FORM THÊM NHÂN VIÊN
    @GetMapping("/add-staff")
    public String showAddStaffForm(Model model) {
        model.addAttribute("newStaff", new User());
        return "admin/add_staff";
    }

    // ✅ XỬ LÝ THÊM NHÂN VIÊN
    @PostMapping("/add-staff")
    @Transactional
    public String addStaff(@ModelAttribute("newStaff") User formUser,
                           RedirectAttributes redirectAttributes) {

        try {
            User newUser = new User();
            newUser.setId(null);
            newUser.setUsername(formUser.getUsername());
            newUser.setPassword(formUser.getPassword());
            newUser.setFullName(formUser.getFullName());
            newUser.setEmail(formUser.getEmail());
            newUser.setPhone(formUser.getPhone());
            newUser.setBirthday(formUser.getBirthday());
            newUser.setGender(formUser.getGender());
            newUser.setAddress(formUser.getAddress());
            newUser.setRole("Staff");

            userRepository.save(newUser);

            redirectAttributes.addFlashAttribute("success", "✅ Thêm nhân viên thành công!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Username hoặc email đã tồn tại!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi khi thêm nhân viên: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}

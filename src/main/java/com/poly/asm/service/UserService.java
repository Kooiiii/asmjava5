package com.poly.asm.service;

import com.poly.asm.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // 🟩 Đăng ký user mới
    User registerUser(User user);

    // 🟩 Tìm user theo username (dùng cho đăng nhập)
    Optional<User> findByUsername(String username);

    // 🟩 Tìm user theo email
    Optional<User> findByEmail(String email);

    // 🟩 Tìm user theo ID
    Optional<User> findById(Integer id);

    // 🟩 Lấy tất cả users
    List<User> findAllUsers();

    // 🟩 Lưu user (cả tạo mới và cập nhật)
    User saveUser(User user);

    // 🟩 Xóa user
    void deleteUser(Integer id);

    // 🟩 Kiểm tra username đã tồn tại
    boolean existsByUsername(String username);

    // 🟩 Kiểm tra email đã tồn tại
    boolean existsByEmail(String email);

    // ✅ Sửa lại hàm này cho đúng logic update profile
    User updateUserProfile(User user);

    // ✅ Sửa lại chữ ký hàm đổi mật khẩu cho khớp Controller
    void changePassword(String username, String currentPassword, String newPassword);
}

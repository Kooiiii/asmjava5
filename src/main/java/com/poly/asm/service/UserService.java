package com.poly.asm.service;

import com.poly.asm.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // Chức năng cho người dùng
    User registerUser(User user);
    Optional<User> findByUsername(String username);

    // Chức năng cho Admin
    List<User> findAllUsers();
    User saveUser(User user); // Dùng cho cả tạo mới và cập nhật
    void deleteUser(Integer id);
}
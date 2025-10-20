package com.poly.asm.service.impl;

import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        // Kiểm tra username đã tồn tại
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // Kiểm tra email đã tồn tại
        if (user.getEmail() != null && !user.getEmail().isEmpty() &&
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Gán vai trò mặc định
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("Customer"); // Customer, Admin, Staff
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public boolean changePassword(User currentUser, String oldPassword, String newPassword) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        // ⚠️ So sánh mật khẩu (chưa mã hóa)
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() == null) {
            // Kiểm tra username trùng
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại!");
            }
        } else {
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

            // Kiểm tra username/email trùng (trừ chính nó)
            userRepository.findByUsername(user.getUsername())
                    .ifPresent(u -> {
                        if (!u.getId().equals(user.getId())) {
                            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
                        }
                    });

            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                userRepository.findByEmail(user.getEmail())
                        .ifPresent(u -> {
                            if (!u.getId().equals(user.getId())) {
                                throw new RuntimeException("Email đã được sử dụng!");
                            }
                        });
            }

            // Giữ mật khẩu cũ nếu không thay đổi
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User không tồn tại!");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // ✅ Cập nhật thông tin hồ sơ người dùng (Profile)
    @Override
    public User updateProfile(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());

        return userRepository.save(existingUser);
    }
}

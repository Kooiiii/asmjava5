package com.poly.asm.service.impl;

import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // Sử dụng @Lazy và constructor injection
    public UserServiceImpl(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Gán vai trò mặc định - SỬA LẠI để khớp với database
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("Customer"); // Không có ROLE_ prefix
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
        // Nếu là user mới (id null)
        if (user.getId() == null) {
            // Kiểm tra username trùng
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại!");
            }
            // Mã hóa mật khẩu cho user mới
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Nếu là cập nhật user
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

            // Kiểm tra username trùng (trừ chính nó)
            userRepository.findByUsername(user.getUsername())
                    .ifPresent(u -> {
                        if (!u.getId().equals(user.getId())) {
                            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
                        }
                    });

            // Kiểm tra email trùng (trừ chính nó)
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
            } else {
                // Mã hóa mật khẩu mới
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        // Kiểm tra user có tồn tại không
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
}
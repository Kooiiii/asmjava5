package com.poly.asm.service.impl;

import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.User;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() &&
                userRepository.existsByEmail(user.getEmail())) { // Dùng existsByEmail
            throw new RuntimeException("Email đã được sử dụng!");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("Customer");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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
        User existingUser = null;
        if (user.getId() != null) {
            existingUser = userRepository.findById(user.getId()).orElse(null);
        }

        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            if (user.getId() == null || !u.getId().equals(user.getId())) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại!");
            }
        });

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
                if (user.getId() == null || !u.getId().equals(user.getId())) {
                    throw new RuntimeException("Email đã được sử dụng!");
                }
            });
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {

            if (!user.getPassword().equals("***")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else if (existingUser != null) {
                user.setPassword(existingUser.getPassword());
            }
        } else if (existingUser != null) {
            user.setPassword(existingUser.getPassword());
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

    @Override
    public User updateUserProfile(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        return userRepository.save(existingUser);
    }
}
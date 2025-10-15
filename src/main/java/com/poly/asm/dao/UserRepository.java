package com.poly.asm.dao;

import com.poly.asm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // tìm user khi đăng nhập. Optional để tránh lỗi NullPointerException.
    Optional<User> findByUsername(String username);
    // Dùng để kiểm tra email có tồn tại chưa
    Optional<User> findByEmail(String email);
    // Tìm tất cả người dùng theo role.
    @Query("SELECT u FROM User u WHERE u.role = ?1")
    List<User> findAllByRole(String role);
}

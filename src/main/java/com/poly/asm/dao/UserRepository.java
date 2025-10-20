package com.poly.asm.dao;

import com.poly.asm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Tìm user theo username
    Optional<User> findByUsername(String username);

    // Tìm user theo email
    Optional<User> findByEmail(String email);

    // Tìm users theo role
    @Query("SELECT u FROM User u WHERE u.role = ?1")
    List<User> findAllByRole(String role);

    // Kiểm tra tồn tại
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Tìm users với phân trang (tùy chọn)
    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findAllOrderByIdDesc();
}
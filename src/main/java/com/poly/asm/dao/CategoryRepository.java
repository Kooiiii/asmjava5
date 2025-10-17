package com.poly.asm.dao;

import com.poly.asm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Các phương thức CRUD cơ bản (findAll, findById, save, delete) đã đủ dùng
    // cho việc quản lý danh mục trong trang admin.
}
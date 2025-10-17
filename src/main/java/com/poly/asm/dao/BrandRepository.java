package com.poly.asm.dao;

import com.poly.asm.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    // Tương tự Category, các phương thức cơ bản đã đủ dùng.
}
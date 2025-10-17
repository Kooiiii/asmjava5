package com.poly.asm.dao;

import com.poly.asm.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    // Các phương thức cơ bản là đủ.
}
package com.poly.asm.dao;

import com.poly.asm.entity.Color;
import com.poly.asm.entity.Product;
import com.poly.asm.entity.ProductVariant;
import com.poly.asm.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    Optional<ProductVariant> findByProductAndSizeAndColor(Product product, Size size, Color color);
}

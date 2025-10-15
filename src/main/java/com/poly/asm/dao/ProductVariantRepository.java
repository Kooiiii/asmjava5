package com.poly.asm.dao;

import com.poly.asm.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    // Tìm tất cả các biến thể của một sản phẩm
    List<ProductVariant> findByProductId(Integer productId);
     // Tìm một biến thể cụ thể dựa trên sản phẩm, màu sắc và kích thước.
    Optional<ProductVariant> findByProductIdAndColorIdAndSizeId(Integer productId, Integer colorId, Integer sizeId);
}

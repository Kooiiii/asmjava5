package com.poly.asm.dao;

import com.poly.asm.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // Tìm tất cả sản phẩm trong giỏ hàng của một khách hàng
    List<CartItem> findByCustomerId(Integer customerId);

    // Tìm một sản phẩm cụ thể trong giỏ hàng của khách hàng (để cập nhật số lượng)
    Optional<CartItem> findByCustomerIdAndProductVariantId(Integer customerId, Integer productVariantId);
}
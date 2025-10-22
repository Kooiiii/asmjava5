package com.poly.asm.dao;

import com.poly.asm.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCustomerId(Integer customerId);
    Optional<CartItem> findByCustomerIdAndProductVariantId(Integer customerId, Integer productVariantId);
    Optional<CartItem> findByIdAndCustomerId(Integer id, Integer customerId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.customer.id = :customerId")
    void deleteByCustomerId(@Param("customerId") Integer customerId);
    @Override
    void deleteById(Integer id);
}
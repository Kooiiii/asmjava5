package com.poly.asm.dao;

import com.poly.asm.entity.WarehouseReceipt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WarehouseReceiptRepository extends JpaRepository<WarehouseReceipt, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM WarehouseReceipt w WHERE w.productVariant.id = :variantId")
    void deleteAllByProductVariantId(@Param("variantId") Integer variantId);
}
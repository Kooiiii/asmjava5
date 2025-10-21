package com.poly.asm.dao;

import com.poly.asm.entity.WarehouseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WarehouseReceiptRepository extends JpaRepository<WarehouseReceipt, Integer> {
}
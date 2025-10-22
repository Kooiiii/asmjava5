package com.poly.asm.dao;

import com.poly.asm.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {
    
    // THÊM METHOD NÀY
    List<InvoiceItem> findByInvoiceId(Integer invoiceId);
    
    // Các method khác nếu có
}
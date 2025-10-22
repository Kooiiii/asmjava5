package com.poly.asm.dao;

import com.poly.asm.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Optional<Invoice> findByIdAndCustomerId(Integer id, Integer customerId);
}

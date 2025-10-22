package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "HD")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHD")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "MaKH")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private User staff;

    @Column(name = "NgayLap")
    private LocalDateTime createdDate;

    @Column(name = "TrangThai")
    private String status;

    @Column(name = "HinhThucTT")
    private String paymentMethod;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvoiceItem> invoiceItems;

    // Method để tính tổng tiền từ chi tiết hóa đơn
    public Double getTotalAmount() {
        if (invoiceItems != null) {
            return invoiceItems.stream()
                .mapToDouble(item -> item.getUnitPrice().doubleValue() * item.getQuantity())
                .sum();
        }
        return 0.0;
    }
}

package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "HDCT")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHDCT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "MaHD")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "MaBienThe")
    private ProductVariant productVariant;

    @Column(name = "SoLuong")
    private Integer quantity;

    @Column(name = "DonGia")
    private BigDecimal unitPrice;
}
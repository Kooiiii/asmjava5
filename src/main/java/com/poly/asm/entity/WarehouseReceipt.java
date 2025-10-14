package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "NhapKho")
public class WarehouseReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNhap")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "NgayNhap")
    private Date importDate;
    @Column(name = "SoLuong")
    private Integer quantity;
    @Column(name = "DonGia", precision = 10, scale = 2)
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "MaNV")
    private User staff;
    @ManyToOne
    @JoinColumn(name = "MaBienThe")
    private ProductVariant productVariant;
}
package com.poly.asm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "SanPhamBienThe")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBienThe")
    private Integer id;

    @Column(name = "SoLuong")
    private Integer quantity;

    @Column(name = "DonGia", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "MaSP", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "MaKT", nullable = false)
    private Size size;

    @ManyToOne
    @JoinColumn(name = "MaMau", nullable = false)
    private Color color;
}
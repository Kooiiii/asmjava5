package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "HDCT")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHDCT")
    private Integer id;
    @Column(name = "SoLuong")
    private Integer quantity;
    @Column(name = "DonGia", precision = 10, scale = 2)
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "MaHD")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "MaBienThe")
    private ProductVariant productVariant;
    @Transient
    public BigDecimal getTotal() {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}
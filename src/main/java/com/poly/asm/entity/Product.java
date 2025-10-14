package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "SP")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer id;
    @Column(name = "TenSP", length = 100)
    private String name;
    @Column(name = "HinhAnh", length = 255)
    private String image;
    @Column(name = "MoTa", length = 255)
    private String description;
    @ManyToOne
    @JoinColumn(name = "MaDM_SP")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "MaTH")
    private Brand brand;
    @OneToMany(mappedBy = "product")
    private List<ProductVariant> variants;
}
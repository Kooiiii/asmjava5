package com.poly.asm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    private String imageUrl;

    @Column(name = "MoTa", length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "MaDM_SP")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "MaTH")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductVariant> variants = new ArrayList<>();

    public ProductVariant ensurePrimaryVariant() {
        if (variants == null) {
            variants = new ArrayList<>();
        }
        if (variants.isEmpty()) {
            ProductVariant v = new ProductVariant();
            v.setProduct(this);
            variants.add(v);
        } else {
            ProductVariant first = variants.get(0);
            if (first.getProduct() == null) {
                first.setProduct(this);
            }
        }
        return variants.get(0);
    }
}
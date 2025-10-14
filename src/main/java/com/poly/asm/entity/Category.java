package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "DM_SP")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDM_SP")
    private Integer id;
    @Column(name = "TenDM_SP", nullable = false, length = 100)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "KichThuoc")
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKT")
    private Integer id;
    @Column(name = "TenKT", length = 10)
    // S, M, L, XL
    private String name;
}
package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DiaChi")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDC")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "U_MaUser")
    private User user;

    @Column(name = "DiaChi")
    private String address;

    @Column(name = "MacDinh")
    private Boolean isDefault = false;
}
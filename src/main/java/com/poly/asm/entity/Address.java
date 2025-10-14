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
    @Column(name = "DiaChi", length = 255)
    private String detailAddress;
    @Column(name = "MacDinh")
    private Boolean isDefault;
    @ManyToOne
    @JoinColumn(name = "U_MaUser")
    private User user;
}
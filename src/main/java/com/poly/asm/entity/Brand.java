package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ThuongHieu")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTH")
    private Integer id;
    @Column(name = "TenTH", length = 100)
    private String name;
    @Column(name = "MoTa", length = 255)
    private String description;
    @OneToMany(mappedBy = "brand")
    private List<Product> products;
}
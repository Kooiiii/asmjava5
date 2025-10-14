package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "HD")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHD")
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NgayLap")
    private Date orderDate;
    @Column(name = "TrangThai", length = 50)
    private String status;
    @Column(name = "HinhThucTT", length = 50)
    private String paymentMethod;
    @ManyToOne
    @JoinColumn(name = "MaKH")
    private User customer;
    @ManyToOne
    @JoinColumn(name = "MaNV")
    private User staff;
    @ManyToOne
    @JoinColumn(name = "MaDC")
    private Address shippingAddress;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
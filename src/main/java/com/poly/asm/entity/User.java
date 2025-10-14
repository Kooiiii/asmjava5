package com.poly.asm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "U_MaUser")
    private Integer id;
    @Column(name = "U_Name", nullable = false, length = 50)
    private String username;
    @Column(name = "U_Pass", nullable = false, length = 255)
    private String password;
    @Column(name = "U_Role", nullable = false, length = 20)
    private String role;
    @Column(name = "U_Email", length = 100)
    private String email;
    @Column(name = "U_Phone", length = 20)
    private String phone;
    @Column(name = "HoTen", length = 100)
    private String fullName;
    @Temporal(TemporalType.DATE)
    @Column(name = "NgaySinh")
    private Date birthday;
    @Column(name = "GioiTinh")
    private Boolean gender;
    @Column(name = "DiaChi", length = 255)
    private String address;
    @OneToMany(mappedBy = "user")
    private List<Address> addresses;
    @OneToMany(mappedBy = "customer")
    private List<Order> ordersAsCustomer;
    @OneToMany(mappedBy = "staff")
    private List<Order> ordersAsStaff;
    @OneToMany(mappedBy = "customer")
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "staff")
    private List<WarehouseReceipt> warehouseReceipts;
}

package com.poly.asm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3-50 ký tự")
    @Column(name = "U_Name", nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @Column(name = "U_Pass", nullable = false, length = 255)
    private String password;

    @Column(name = "U_Role", nullable = false, length = 20)
    private String role = "Customer";

    @Email(message = "Email không hợp lệ")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Address> addresses;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Order> ordersAsCustomer;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.REMOVE)
    private List<Order> ordersAsStaff;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.REMOVE)
    private List<WarehouseReceipt> warehouseReceipts;

}
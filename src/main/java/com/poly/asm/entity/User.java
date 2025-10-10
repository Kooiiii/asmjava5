package com.poly.asm.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String role;

    public User() {}

    public User(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public User(Long id, String username, String fullName, String email, String phone, String address, String role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

}

//package com.poly.asm.controller;
//
//import lombok.Data;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class AuthController {
//
//    @GetMapping("/auth/login")
//    public String login(){
//        return "login";
//    }
//
//    @GetMapping("/forgot-password")
//    public String forgotPassword(){
//        return "forgot_password";
//    }
//
//    @GetMapping("/sign-up")
//    public String register(Model model){
//        model.addAttribute("user", new UserDto());
//        return "register";
//    }
//
//    @Data
//    public static class UserDto {
//        private String username;
//        private String password;
//        private String fullName;
//        private String email;
//        private String phone;
//        private String address;
//        private String role = "USER";
//
//        public UserDto() {}
//
//        public UserDto(String username, String password, String fullName, String email, String phone, String address) {
//            this.username = username;
//            this.password = password;
//            this.fullName = fullName;
//            this.email = email;
//            this.phone = phone;
//            this.address = address;
//            this.role = "USER";
//        }
//
//        public UserDto(String username, String password, String fullName, String email, String phone, String address, String role) {
//            this.username = username;
//            this.password = password;
//            this.fullName = fullName;
//            this.email = email;
//            this.phone = phone;
//            this.address = address;
//            this.role = role;
//        }
//    }
//}
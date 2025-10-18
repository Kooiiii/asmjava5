package com.poly.asm.config;

import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Chúng ta cần UserDetailsService để Spring Security biết cách tìm user
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tạm thời vô hiệu hóa CSRF
                .authorizeHttpRequests(authorize -> authorize
                        // === CÁC TRANG CÔNG KHAI ===
                        .requestMatchers("/", "/home", "/photos/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/products", "/products/{id}", "/products/search").permitAll()
                        .requestMatchers("/auth/login", "/sign-up", "/register/saveUser", "/forgot-password").permitAll() // Đường dẫn đăng nhập/đăng ký

                        // === CÁC TRANG ADMIN ===
                        .requestMatchers("/admin/**", "/products/add", "/products/edit/**", "/products/delete/**").hasRole("ADMIN") // Yêu cầu quyền ADMIN

                        // === CÁC TRANG CẦN ĐĂNG NHẬP ===
                        .requestMatchers("/profile/**", "/cart/**", "/checkout/**", "/order/**").authenticated() // Yêu cầu đăng nhập

                        // Mọi request khác
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")         // Trang đăng nhập của bạn
                        .loginProcessingUrl("/login")   // URL Spring Security xử lý (khớp với template login.html)
                        .defaultSuccessUrl("/", true)  // Về trang chủ sau khi login thành công
                        .failureUrl("/auth/login?error=true") // Báo lỗi nếu login sai
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Đường dẫn để thực hiện logout
                        .logoutSuccessUrl("/auth/login?logout=true") // Về trang login sau khi logout
                        .permitAll()
                );

        return http.build();
    }
}
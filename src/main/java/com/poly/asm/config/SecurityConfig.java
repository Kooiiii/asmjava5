package com.poly.asm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Đánh dấu đây là một lớp cấu hình
@Configuration
// Kích hoạt tính năng bảo mật web của Spring Security
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Spring sẽ tìm thấy phương thức @Bean này và tạo ra một đối tượng PasswordEncoder.
     * Sau đó, nó có thể được inject (@Autowired) vào bất kỳ đâu trong ứng dụng.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình các quy tắc bảo mật cho các request HTTP.
     * Đây là nơi bạn định nghĩa trang nào cần đăng nhập, trang nào không.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tạm thời vô hiệu hóa CSRF để dễ test
                .authorizeHttpRequests(authorize -> authorize
                        // Cho phép tất cả mọi người truy cập vào các URL này
                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/products/**", "/cart/**").permitAll()
                        .requestMatchers("/login", "/register").permitAll()

                        // Yêu cầu quyền 'ADMIN' để truy cập vào các URL bắt đầu bằng /admin/
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Tất cả các request còn lại đều cần phải được xác thực (đăng nhập)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Cấu hình trang đăng nhập tùy chỉnh
                        .loginPage("/login")           // Đường dẫn đến trang đăng nhập
                        .loginProcessingUrl("/login")  // URL mà form đăng nhập sẽ gửi đến để xử lý
                        .defaultSuccessUrl("/home", true) // URL chuyển hướng sau khi đăng nhập thành công
                        .permitAll()                     // Cho phép tất cả mọi người truy cập trang đăng nhập
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // URL chuyển hướng sau khi đăng xuất
                        .permitAll()
                );

        return http.build();
    }
}
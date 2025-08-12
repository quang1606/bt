package com.example.baitapentitymovies.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.support.MultipartFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AuthenticationProvider authenticationProvider;
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public FilterRegistrationBean<MultipartFilter> filterRegistrationBean() {
        MultipartFilter multipartFilter = new MultipartFilter();
        FilterRegistrationBean<MultipartFilter> registrationBean = new FilterRegistrationBean<>(multipartFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.configure(httpSecurity))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
                .sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // CHO PHÉP CÁC URL CÔNG KHAI
                        .requestMatchers(
                                "/",                   // Trang chủ
                                "/phim-bo/**",         // Các trang phim bộ
                                "/phim-le/**",         // Các trang phim lẻ
                                "/phim-chieu-rap/**",  // Các trang phim chiếu rạp
                                "/phim/{id}/{slug}",   // Trang chi tiết phim
                                "/xem-phim/**",        // Trang xem phim
                                "/tin-tuc/**",         // Trang tin tức
                                "/chi-tiet-tin-tuc/**",// Trang chi tiết tin tức
                                "/dangnhap-dangky",    // Trang đăng nhập, đăng ký
                                "/api/auth/**",        // API xác thực
                                "/css/**",             // Cho phép truy cập file CSS
                                "/javascrip/**",       // Cho phép truy cập file JS
                                "/img/**"              // Cho phép truy cập file ảnh
                        ).permitAll()
                        // CÁC REQUEST CÒN LẠI MỚI YÊU CẦU XÁC THỰC
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider);
        ;

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}

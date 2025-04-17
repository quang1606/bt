package com.example.baitapentitymovies.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthorizationInterceptor authorizationInterceptor;
    private final AuthenticationInterceptor authenticationProvider;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationProvider)
                .addPathPatterns("/api/reviews", "/api/reviews/**","/api/favourite", "/api/favourite/**","/phim-yeu-thich");
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/admin", "/api/admin/**");

    }


}

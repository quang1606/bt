package com.example.baitapentitymovies.config;

import com.example.baitapentitymovies.model.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String,Object> errorDetails=new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("error", "unauthorized");
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("message", authException.getMessage());
        response.getOutputStream().println(objectMapper.writeValueAsString(errorDetails));
    }
}

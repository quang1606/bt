package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.exception.BadRequestException;
import com.example.baitapentitymovies.model.request.LoginRequest;
import com.example.baitapentitymovies.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    public void login(LoginRequest loginRequest) {
        User user =  userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new BadRequestException("Tai khoan hoac mat khau khong dung"));
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Tai khoan hoac mat khong dung");
        }
        // luu lai redis, cooke, database
        httpSession.setAttribute("user", user);
    }


    public void logout() {
        httpSession.removeAttribute("user");
    }
}

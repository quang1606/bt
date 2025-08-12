package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.CustomUserDetails;
import com.example.baitapentitymovies.entity.RefreshToken;
import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.entity.VerificationToken;
import com.example.baitapentitymovies.exception.BadRequestException;
import com.example.baitapentitymovies.model.enums.Role;
import com.example.baitapentitymovies.model.request.LoginRequest;
import com.example.baitapentitymovies.model.request.RegistrationRequest;
import com.example.baitapentitymovies.model.response.LoginResponse;
import com.example.baitapentitymovies.repository.UserRepository;
import com.example.baitapentitymovies.repository.VerificationTokenRepository;
import com.example.baitapentitymovies.uitils.JwtTokenUtil;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final VerificationTokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(token);
        return new LoginResponse(token, userDetails.getUsername(), userDetails.getUser().getRole());
    }

    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            String accessToken=  authHeader.substring(7);
            long remainingExpiration = jwtTokenUtil.getRemainingExpiration(accessToken);
            if(remainingExpiration > 0){
                tokenBlacklistService.blacklist(accessToken,remainingExpiration);
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            refreshTokenService.deleteByToken(username);
        }

        System.out.println("User requested logout. Client should clear token.");
    }


    public User register(RegistrationRequest registrationRequest) {
        if (userRepository.existsByname(registrationRequest.getUsername())){
            throw new BadRequestException("tai khoan da ton tai");
        }
       if(userRepository.existsByEmail(registrationRequest.getEmail())){
            throw new BadRequestException("Email da ton tai ");
       }
       User user = new User();
       user.setEmail(registrationRequest.getEmail());
       user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
       user.setName(registrationRequest.getUsername());
       user.setRole(Role.USERS);
    return userRepository.save(user);
    }


    public void createVerificationTokenForUser(User user, String token) {
        // Nếu user đã có token cũ (ví dụ đăng ký lại nhưng chưa xác thực), xóa token cũ
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        VerificationToken myToken = new VerificationToken(token,user);
        tokenRepository.save(myToken);

    }

    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).orElse(null);
    }

    @Transactional
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Token không hợp lệ hoặc không tìm thấy."));

        if (verificationToken.isExpired()) {
            // Xóa token hết hạn và thông báo
            tokenRepository.delete(verificationToken);
            return "TOKEN_EXPIRED"; // Hoặc ném một exception tùy chỉnh
        }

        User user = verificationToken.getUser();
        if (user.isEnabled()) {
            return "TOKEN_ALREADY_VERIFIED"; // Tài khoản đã được kích hoạt trước đó
        }

        user.setEnabled(true);
        userRepository.save(user); // Kích hoạt user

        // Token đã được sử dụng, nên xóa nó đi để không dùng lại được
        tokenRepository.delete(verificationToken);
        return "TOKEN_VALID";
    }

}

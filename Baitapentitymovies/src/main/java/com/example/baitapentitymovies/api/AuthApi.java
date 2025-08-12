package com.example.baitapentitymovies.api;

import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.event.OnRegistrationCompleteEvent;
import com.example.baitapentitymovies.model.request.LoginRequest;
import com.example.baitapentitymovies.model.request.RegistrationRequest;
import com.example.baitapentitymovies.model.response.LoginResponse;
import com.example.baitapentitymovies.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
private final AuthService authService;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest registrationRequest, HttpServletRequest request) {
        User registeredUser = authService.register(registrationRequest); // AuthService giờ trả về User
        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), appUrl));
        } catch (Exception me) {

            System.err.println("Error publishing registration event: " + me.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt tài khoản của bạn."));
    }


    @GetMapping("/registrationConfirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token, WebRequest request) {
        Locale locale = request.getLocale(); // Để lấy ngôn ngữ nếu cần cho message
        String result = authService.validateVerificationToken(token);

        switch (result) {
            case "TOKEN_VALID":
                // Chuyển hướng đến trang login với thông báo thành công
                // Hoặc trả về một trang HTML thông báo thành công
                // return "redirect:/login?verificationSuccess=true"; (Nếu dùng Spring MVC truyền thống)
                return ResponseEntity.ok(Map.of("message", "Xác thực tài khoản thành công! Bạn có thể đăng nhập."));
            case "TOKEN_INVALID":
                // return "redirect:/badUser.html?message=invalidToken&lang=" + locale.getLanguage();
                return ResponseEntity.badRequest().body(Map.of("message", "Token xác thực không hợp lệ."));
            case "TOKEN_EXPIRED":
                // return "redirect:/badUser.html?message=expiredToken&lang=" + locale.getLanguage();
                return ResponseEntity.status(HttpStatus.GONE).body(Map.of("message", "Token xác thực đã hết hạn. Vui lòng yêu cầu token mới.")); // 410 GONE
            case "TOKEN_ALREADY_VERIFIED":
                return ResponseEntity.ok(Map.of("message", "Tài khoản của bạn đã được xác thực trước đó."));
            default:
                // Trường hợp không xác định
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Lỗi không xác định trong quá trình xác thực."));
        }
    }
}

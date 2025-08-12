package com.example.baitapentitymovies.listener;

import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.event.OnRegistrationCompleteEvent;
import com.example.baitapentitymovies.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component

public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private static final Logger log = LoggerFactory.getLogger(RegistrationListener.class);
    private final AuthService authService;
    private final MessageSource messages;
    private final JavaMailSender mailSender;
    @Autowired

    public RegistrationListener(AuthService authService, MessageSource messages, JavaMailSender mailSender) {
        this.authService = authService;
        this.messages = messages;
        this.mailSender = mailSender;
    }
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        authService.createVerificationTokenForUser(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Xác nhận đăng ký tài khoản"; // "Registration Confirmation"
        String confirmationUrl = event.getAppUrl() + "/api/auth/registrationConfirm?token=" + token;
        String messageText = "Cảm ơn bạn đã đăng ký. Vui lòng nhấp vào liên kết dưới đây để kích hoạt tài khoản của bạn:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(messageText + "\r\n" + confirmationUrl);


        try {
            mailSender.send(email);
            log.info("Verification email sent to {}", recipientAddress);
        } catch (Exception e) {
            log.error("Error sending verification email to {}: {}", recipientAddress, e.getMessage());
            // Cân nhắc: throw một custom exception ở đây nếu việc gửi mail thất bại là nghiêm trọng
            // hoặc retry, hoặc thông báo cho admin.
        }
    }

}

package com.example.baitapentitymovies.event;

import com.example.baitapentitymovies.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;
@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl; // URL của ứng dụng, ví dụ: http://localhost:8080
    private Locale locale;
    private User user;
    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        super(user); // Source của event là user object
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

}

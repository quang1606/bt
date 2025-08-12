package com.example.baitapentitymovies.model.response;

import com.example.baitapentitymovies.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private Role role;
        // Thêm các thông tin khác nếu cần
}

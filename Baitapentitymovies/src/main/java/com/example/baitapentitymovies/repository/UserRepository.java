package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByRole(Role role);

    boolean existsByPhone(String phoneNumber);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(@NotBlank(message = "Email không được để trống") String email);



    boolean existsByname(@NotBlank(message = "Username không được để trống") @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự") String username);
}

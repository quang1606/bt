package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByRole(Role role);

    boolean existsByPhone(String phoneNumber);
}

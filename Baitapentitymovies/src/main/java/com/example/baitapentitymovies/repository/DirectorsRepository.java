package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Directors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorsRepository extends JpaRepository<Directors, Integer> {
}
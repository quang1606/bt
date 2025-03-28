package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository extends JpaRepository<Genres, Integer> {
}
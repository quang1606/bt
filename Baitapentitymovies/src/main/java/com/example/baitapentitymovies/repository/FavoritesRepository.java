package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
}
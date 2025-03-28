package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}

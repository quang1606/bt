package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {
    Page<Reviews> findByMovie_Id(Integer movieId, Pageable pageable);
}

package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Episodes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EpisodesRepository extends JpaRepository<Episodes, Integer> {
   

    Episodes findByMovie_IdAndDisplayOrderAndStatus(Integer id, int tap, boolean status);

    List<Episodes> findByMovie_IdAndStatusOrderByDisplayOrderAsc(Integer id, Boolean status);

    Page<Episodes> findByMovie_IdAndStatus(int movieId, Boolean status, Pageable pageable);
}
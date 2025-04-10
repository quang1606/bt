package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
    Page<Posts> findPostsByStatus(Boolean status, Pageable pageable);

    Posts findPostsByStatusAndId(Boolean status, int id);
}
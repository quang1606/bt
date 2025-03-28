package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
}
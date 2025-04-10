package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.Posts;
import com.example.baitapentitymovies.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    public Page<Posts> finByPost(Boolean status, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("publishedAt").descending());
        return postsRepository.findPostsByStatus(status, pageable);
    }




    public Posts finByPostStatusAndId(boolean b, Integer id) {
        Posts posts = postsRepository.findPostsByStatusAndId(b,id);
        return posts;
    }
}

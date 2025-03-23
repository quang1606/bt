package com.example.baitapentitymovies.service;


import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.model.enums.MovieType;
import com.example.baitapentitymovies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Page<Movie> findByType(MovieType type, Boolean status, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("publishDate").descending());
        Page<Movie> moviePage = movieRepository.findByTypeAndStatus(type, status, pageable);
        return moviePage;
    }
    public List<Movie> findByStatus(Boolean status, Integer page) {
        List<Movie> moviePage = movieRepository.findByStatus(status,page);
        return moviePage;
    }
    public Movie findByIdSlugStatus(int id, String slug) {
        Movie moviePage = movieRepository.findByIdSlugStatus(slug, id, true);
        return moviePage;
    }
//public Movie findMovieDetails(Integer id, String slug) {
//    return movieRepository.findByIdAndSlugAndStatus(id, slug, true);
//}
}

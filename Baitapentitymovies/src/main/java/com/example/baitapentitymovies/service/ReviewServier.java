package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.entity.Reviews;
import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.exception.BadRequestException;
import com.example.baitapentitymovies.model.request.CreateReviewRequest;
import com.example.baitapentitymovies.model.request.UpdateReviewRequest;
import com.example.baitapentitymovies.repository.MovieRepository;
import com.example.baitapentitymovies.repository.ReviewsRepository;
import com.example.baitapentitymovies.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServier {
    private final HttpSession session;
    private final ReviewsRepository reviewsRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    public Page<Reviews> getReviewsByMovie(Integer movieId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending());
        return reviewsRepository.findByMovie_Id(movieId,pageable);
    }

    public Reviews createReview(CreateReviewRequest request) {
        // TODO:fix login user
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }

           Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(()->new RuntimeException("Khong tim thay id co"+request.getMovieId()));
        Reviews reviews = Reviews.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .movie(movie)
                .build();
        return reviewsRepository.save(reviews);
    }

    public Reviews updateReview(Integer id, UpdateReviewRequest request) {
        // TODO:fix login user
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }

        Reviews reviews = reviewsRepository.findById(id).orElseThrow(()->new RuntimeException("Khong tim thay id co"+id));

        if (!reviews.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Khong co quyen cap nhat review");
        }
        reviews.setRating(request.getRating());
        reviews.setContent(request.getContent());
         return  reviewsRepository.save(reviews);

    }

    public void deleteReview(Integer id) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }
        Reviews reviews = reviewsRepository.findById(id).orElseThrow(()->new RuntimeException("Khong tim thay id co"+id));
        if (!reviews.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Khong co quyen xoa review");
        }
       reviewsRepository.delete(reviews);

    }
}

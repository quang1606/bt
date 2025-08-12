package com.example.baitapentitymovies.api;

import com.example.baitapentitymovies.entity.Reviews;
import com.example.baitapentitymovies.model.request.CreateReviewRequest;
import com.example.baitapentitymovies.model.request.UpdateReviewRequest;
import com.example.baitapentitymovies.service.ReviewServier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/api/reviews")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class ReviewApi {
    private final ReviewServier  reviewServier;
        @GetMapping("/api/reviewss/get")
        public ResponseEntity<?> getReviews(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "18") Integer pageSize,@RequestParam Integer movieId) {
            Page<Reviews> reviewsPage = reviewServier.getReviewsByMovie(movieId,page,pageSize);
            return ResponseEntity.ok(reviewsPage);
        }

    @PostMapping("/api/reviews")
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest request) {
        Reviews review = reviewServier.createReview(request);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
       reviewServier.deleteReview(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/reviews/{id}")

    public ResponseEntity<?> updateReview( @PathVariable Integer id,@RequestBody UpdateReviewRequest request) {
        Reviews review = reviewServier.updateReview(id,request);
        return ResponseEntity.ok(review);
    }
}

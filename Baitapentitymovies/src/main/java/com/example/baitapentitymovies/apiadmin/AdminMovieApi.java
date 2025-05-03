package com.example.baitapentitymovies.apiadmin;

import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.model.request.CreateMovieRequest;
import com.example.baitapentitymovies.model.request.UpdateMovieRequest;
import com.example.baitapentitymovies.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/movies")
public class AdminMovieApi {
    private final MovieService movieService;
    @GetMapping()
    public ResponseEntity<?> getAllMovies(@RequestParam (defaultValue = "1") Integer page, @RequestParam (defaultValue = "10") Integer pageSize) {

        Page<Movie> moviePage = movieService.findByAll(page,pageSize);
        return ResponseEntity.ok(moviePage);
    }
    @PostMapping ("")
    public ResponseEntity<?> addMovie(@RequestBody @Valid CreateMovieRequest createMovieRequest) {
        Movie movie = movieService.createMovie(createMovieRequest);
        return ResponseEntity.ok(movie);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Integer id, @RequestBody @Valid UpdateMovieRequest updateMovieRequest) {
        Movie movie = movieService.updateRequest(updateMovieRequest,id);
        return ResponseEntity.ok(movie);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Integer id) {

        movieService.daleteMovie(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping ("/{id}/upload-thumbnail")
    public ResponseEntity<?> uploadThumbnail (@RequestParam MultipartFile file, @PathVariable Integer id ) {

        return ResponseEntity.ok(movieService.uploadThumbnail(id,file));
    }
}

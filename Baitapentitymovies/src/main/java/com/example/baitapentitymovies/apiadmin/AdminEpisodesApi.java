package com.example.baitapentitymovies.apiadmin;

import com.example.baitapentitymovies.entity.Episodes;
import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.model.request.CreateEpisodeRequest;
import com.example.baitapentitymovies.model.request.CreateMovieRequest;
import com.example.baitapentitymovies.model.request.UpdateEpisodeRequest;
import com.example.baitapentitymovies.model.request.UpdateMovieRequest;
import com.example.baitapentitymovies.service.EpisodesService;
import com.example.baitapentitymovies.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/episodes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEpisodesApi {
    private final MovieService movieService;
    private final EpisodesService episodesService;
    @GetMapping("/{movieId}")
    public ResponseEntity<?> getAllEpisodes(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @PathVariable("movieId") Integer movieId) {

        Page<Episodes> moviePage = episodesService.findByMovieId(page, pageSize, movieId);
        return ResponseEntity.ok(moviePage);
    }

    @PostMapping("")
    public ResponseEntity<?> addEpisodes(@RequestBody   CreateEpisodeRequest createEpisodeRequest) {
        Episodes episodes = episodesService.createEpisodes(createEpisodeRequest);
        return ResponseEntity.ok(episodes);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Integer id, @RequestBody @Valid UpdateEpisodeRequest updateEpisodeRequest) {
        Episodes episodes = episodesService.updateEpisodes(updateEpisodeRequest,id);
        return ResponseEntity.ok(episodes);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Integer id) {

        episodesService.daleteEpisodes(id);
        return ResponseEntity.ok().build();
    }
}

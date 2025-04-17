package com.example.baitapentitymovies.api;

import com.example.baitapentitymovies.entity.Favorites;
import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favourite")
@RequiredArgsConstructor
public class FavouriteApi {
    private final   FavouriteService favouriteService;
    @GetMapping()
    public ResponseEntity<?> getFavourite( @RequestParam (defaultValue = "1") Integer page,@RequestParam(defaultValue = "12") Integer pageSize) {
        Page<Movie> moviePage =  favouriteService.getFavouriteMovie(page,pageSize);
        return ResponseEntity.ok(moviePage);
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> postFavouriteMovie(@PathVariable int id) {
        Favorites favorites = favouriteService.postFavouriteMovie(id);
        return ResponseEntity.ok(favorites);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavouriteMovie(@PathVariable int id) {
        favouriteService.deleteFavouriteMovie(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllFavouriteMovie() {
        favouriteService.deleteAllFavouriteMovie();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}/{movieid}")
    public ResponseEntity<?> getMovieById(@PathVariable int movieid) {
        boolean isFavourite = favouriteService.isFavourite(movieid);
        return ResponseEntity.ok(isFavourite);
    }

}

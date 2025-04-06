package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.Favorites;
import com.example.baitapentitymovies.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
    @Query("SELECT m FROM Favorites f JOIN Movie m ON f.movie.id = m.id JOIN User u ON f.user.id = u.id WHERE u.id = :userId")
    Page<Movie> findMoviesByUserId(@Param("userId") int userId, Pageable pageable);

    Favorites findFavoritesByMovie_IdAndUser_Id(int movieId, Integer userId);

    List<Favorites> findByUserId(Integer userId);
}
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
    // lay  dnah sach phim  yeu thích chi tiêt theo user
    @Query("SELECT m FROM Favorites f JOIN Movie m ON f.movie.id = m.id JOIN User u ON f.user.id = u.id WHERE u.id = :userId")
    Page<Movie> findMoviesByUserId(@Param("userId") int userId, Pageable pageable);

    // tim kiem phim yeu thích theo user
    Favorites findFavoritesByMovie_IdAndUser_Id(int movieId, Integer userId);


    //tim kiem tất cả phim yêu thích theo userid
    List<Favorites> findByUserId(Integer userId);

    //kiem tra trang thai phim yeu thhics
//    @Query("select count(*) from Favorites f where f.movie.id = :movieId and f.user.id = :userId")
//    long countByMovieIdAndUserId(@Param("movieId") int movieId, @Param("userId") Integer userId);


//kiem tra trang thai phim yeu thhics (C2: kiem tra xem co ton tia ban ghi khong)
    boolean existsByMovie_IdAndUser_Id(int movieId, Integer userId);
}
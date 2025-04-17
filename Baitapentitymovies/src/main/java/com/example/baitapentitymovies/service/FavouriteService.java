package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.Favorites;
import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.exception.BadRequestException;
import com.example.baitapentitymovies.repository.FavoritesRepository;
import com.example.baitapentitymovies.repository.MovieRepository;
import com.example.baitapentitymovies.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService {
private final FavoritesRepository favoritesRepository;
private final UserRepository userRepository;
private final MovieRepository movieRepository;
private final HttpSession session;
    public Page<Movie> getFavouriteMovie( Integer page, Integer pageSize) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending());
        Page<Movie> moviePage = favoritesRepository.findMoviesByUserId(user.getId(),pageable);
        return moviePage;
    }

    public Favorites postFavouriteMovie(int id) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }
        Movie movie1 = movieRepository.findById(id).orElseThrow(()->new RuntimeException("Khong tim thay phim id co"+id));
        Favorites favorites = Favorites.builder()
                .createdAt(LocalDateTime.now())
                .movie(movie1)
                .user(user)
                .build();
       return favoritesRepository.save(favorites);
    }

    public void deleteFavouriteMovie(int id) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }
        Favorites favorites = favoritesRepository.findFavoritesByMovie_IdAndUser_Id(id,user.getId());
        if(!favorites.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Khong co quyen xoa review");
        }
        favoritesRepository.delete(favorites);
    }

    public void deleteAllFavouriteMovie() {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }
        List<Favorites> favoritesList = favoritesRepository.findByUserId(user.getId());
        if (!favoritesList.get(0).getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Khong co quyen xoa review");
        }
        favoritesRepository.deleteAll(favoritesList);
    }

//    public boolean isFavourite( int movieid) {
//        Integer userId=1;
//        Movie movie1 = movieRepository.findById(movieid).orElseThrow(()->new RuntimeException("Khong tim thay phim id co"+movieid));
//        long count = favoritesRepository.countByMovieIdAndUserId(movie1.getId(), userId);
//        return count > 0;
//
//    }

    public boolean isFavourite(int movieId) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Ban chua dnag nhap!");
        }
        return favoritesRepository.existsByMovie_IdAndUser_Id(movieId, user.getId());
    }
}

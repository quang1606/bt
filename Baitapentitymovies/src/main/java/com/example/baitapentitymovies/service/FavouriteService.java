package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.Favorites;
import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.repository.FavoritesRepository;
import com.example.baitapentitymovies.repository.MovieRepository;
import com.example.baitapentitymovies.repository.UserRepository;
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
    public Page<Movie> getFavouriteMovie( Integer page, Integer pageSize) {
        Integer userId=1;
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending());
        Page<Movie> moviePage = favoritesRepository.findMoviesByUserId(userId,pageable);
        return moviePage;
    }

    public Favorites postFavouriteMovie(int id) {
        Integer userId=1;
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Khong tim thay id co"+userId));
        Movie movie1 = movieRepository.findById(id).orElseThrow(()->new RuntimeException("Khong tim thay phim id co"+id));
        Favorites favorites = Favorites.builder()
                .createdAt(LocalDateTime.now())
                .movie(movie1)
                .user(user)
                .build();
       return favoritesRepository.save(favorites);
    }

    public void deleteFavouriteMovie(int id) {
        Integer userId=1;
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Khong tim thay id co"+userId));

        Favorites favorites = favoritesRepository.findFavoritesByMovie_IdAndUser_Id(id,userId);
        if(favorites.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Khong co quyen xoa review");
        }
        favoritesRepository.delete(favorites);
    }

    public void deleteAllFavouriteMovie() {
        Integer userId=1;
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Khong tim thay id co"+userId));
        List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
        if (!favoritesList.get(0).getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Khong co quyen xoa review");
        }
        favoritesRepository.deleteAll(favoritesList);
    }
}

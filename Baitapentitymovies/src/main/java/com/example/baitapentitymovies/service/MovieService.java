package com.example.baitapentitymovies.service;


import com.example.baitapentitymovies.entity.*;
import com.example.baitapentitymovies.exception.BadRequestException;
import com.example.baitapentitymovies.model.enums.MovieType;
import com.example.baitapentitymovies.model.enums.Role;
import com.example.baitapentitymovies.model.request.CreateMovieRequest;
import com.example.baitapentitymovies.model.request.UpdateMovieRequest;
import com.example.baitapentitymovies.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final HttpSession session;
    private final CountryRepository countryRepository;
    private final GenresRepository genresRepository;
    private final ActorsRepository actorsRepository;
    private final DirectorsRepository directorsRepository;
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
    public List<Movie> findBySlugAndStatus(MovieType type, Boolean status,Integer limit) {
        List<Movie> moviePage = movieRepository.findBySlugAndStatus(type.name(), status, limit);
        return moviePage;
    }
    public List<Episodes> findEpisodesByMovieTypeSorted(int id,Boolean status) {
        List<Episodes> episode = movieRepository.findEpisodesByMovieTypeSorted(id,status);
        return episode;
    }

    public Page<Movie> findByTypeAndStatusAndRating(MovieType type, boolean b, int i) {
        Pageable pageable = PageRequest.of(1,i,Sort.by("rating").descending());
        Page<Movie> moviePage = movieRepository.findByTypeAndStatus(type, b, pageable);
        return moviePage;
    }

    //Admin

    public Page<Movie> findByStatuss(boolean b, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("publishDate").descending());
        Page<Movie> moviePage = movieRepository.findByStatus(true,pageable);
        return moviePage;
    }


    public Movie createMovie(CreateMovieRequest request) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Bạn chưa đăng nhập");
        }

        if (user.getRole() != Role.ADMIN) {
            throw new BadRequestException("Bạn không có quyền quản trị");
        }
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new IllegalArgumentException("Quốc gia không tồn tại"));

        // Validate and fetch genres
        List<Genres> genres = new ArrayList<>();
        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            genres = genresRepository.findAllById(request.getGenreIds());
            if (genres.size() != request.getGenreIds().size()) {
                throw new IllegalArgumentException("Một hoặc nhiều thể loại không tồn tại.");
            }
        }

        // Validate and fetch actors
        List<Actors> actors = new ArrayList<>();
        if (request.getActorIds() != null && !request.getActorIds().isEmpty()) {
            actors = actorsRepository.findAllById(request.getActorIds());
            if (actors.size() != request.getActorIds().size()) {
                throw new IllegalArgumentException("Một hoặc nhiều diễn viên không tồn tại.");
            }
        }

        // Validate and fetch directors
        List<Directors> directors = new ArrayList<>();
        if (request.getDirectorIds() != null && !request.getDirectorIds().isEmpty()) {
            directors = directorsRepository.findAllById(request.getDirectorIds());
            if (directors.size() != request.getDirectorIds().size()) {
                throw new IllegalArgumentException("Một hoặc nhiều đạo diễn không tồn tại.");
            }
        }

        // Build Movie
        Movie movie = Movie.builder()
                .name(request.getName())
                .trailer(request.getTrailerUrl())
                .description(request.getDescription())
                .releaseYear(request.getReleaseYear())
                .type(request.getType())
                .status(request.getStatus())
                .country(country)
                .genres(genres)
                .actors(actors)
                .directors(directors)
                .build();

        // Save to DB
        return movieRepository.save(movie);

    }

    public Movie updateRequest(UpdateMovieRequest request, Integer id) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Bạn chưa đăng nhập");
        }
        if (user.getRole() != Role.ADMIN) {
            throw new BadRequestException("Bạn không có quyền quản trị");
        }

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy phim với id = " + id));

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new IllegalArgumentException("Quốc gia không tồn tại"));

        List<Genres> genres = request.getGenreIds() == null ? new ArrayList<>() :
                genresRepository.findAllById(request.getGenreIds());
        if (genres.size() != request.getGenreIds().size()) {
            throw new IllegalArgumentException("Một hoặc nhiều thể loại không tồn tại.");
        }

        List<Actors> actors = request.getActorIds() == null ? new ArrayList<>() :
                actorsRepository.findAllById(request.getActorIds());
        if (actors.size() != request.getActorIds().size()) {
            throw new IllegalArgumentException("Một hoặc nhiều diễn viên không tồn tại.");
        }

        List<Directors> directors = request.getDirectorIds() == null ? new ArrayList<>() :
                directorsRepository.findAllById(request.getDirectorIds());
        if (directors.size() != request.getDirectorIds().size()) {
            throw new IllegalArgumentException("Một hoặc nhiều đạo diễn không tồn tại.");
        }

        // Cập nhật movie
        movie.setName(request.getName());
        movie.setDescription(request.getDescription());
        movie.setTrailer(request.getTrailerUrl());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setType(request.getType());
        movie.setStatus(request.getStatus());
        movie.setCountry(country);
        movie.setGenres(genres);
        movie.setActors(actors);
        movie.setDirectors(directors);

        return movieRepository.save(movie);
    }

    public void daleteMovie(Integer id) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new BadRequestException("Bạn chưa đăng nhập");
        }
        if (user.getRole() != Role.ADMIN) {
            throw new BadRequestException("Bạn không có quyền quản trị");
        }

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy phim với id = " + id));
         movieRepository.delete(movie);
    }


}

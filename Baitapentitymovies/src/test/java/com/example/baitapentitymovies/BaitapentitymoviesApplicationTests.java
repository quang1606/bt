package com.example.baitapentitymovies;

import com.example.baitapentitymovies.entity.*;
import com.example.baitapentitymovies.model.enums.MovieType;
import com.example.baitapentitymovies.model.enums.Role;
import com.example.baitapentitymovies.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class BaitapentitymoviesApplicationTests {


        @Autowired
        private ActorsRepository actorRepository;

        @Autowired
        private CountryRepository countryRepository;

        @Autowired
        private DirectorsRepository directorRepository;

        @Autowired
        private EpisodesRepository episodeRepository;

        @Autowired
        private FavoritesRepository favoriteRepository;

        @Autowired
        private GenresRepository genreRepository;

        @Autowired
        private HistoriesRepository historyRepository;

        @Autowired
        private MediasRepository mediaRepository;

        @Autowired
        private MovieRepository movieRepository;

        @Autowired
        private PostsRepository postRepository;

        @Autowired
        private ReviewsRepository reviewRepository;

        @Autowired
        private UserRepository userRepository;

        @Test
        void save_countries() {
            Faker faker = new Faker();
            Slugify slugify = Slugify.builder().build();
            for (int i = 0; i < 10; i++) {
                String name = faker.country().name();
                Country country = Country.builder()
                        .name(name)
                        .slug(slugify.slugify(name))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                countryRepository.save(country);
            }
        }

        @Test
        void save_genres() {
            Faker faker = new Faker();
            Slugify slugify = Slugify.builder().build();
            for (int i = 0; i < 10; i++) {
                String name = faker.leagueOfLegends().champion();
                Genres genre = Genres.builder()
                        .name(name)
                        .slug(slugify.slugify(name))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                genreRepository.save(genre);
            }
        }

        @Test
        void save_actors() {
            Faker faker = new Faker();
            Slugify slugify = Slugify.builder().build();
            for (int i = 0; i < 1000; i++) {
                String name = faker.name().fullName();
                Actors actor = Actors.builder()
                        .name(name)
                        .slug(slugify.slugify(name))
                        .avatar("https://placehold.co/600x400?text=" + name.substring(0, 1).toUpperCase())
                        .bio(faker.lorem().paragraph())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                actorRepository.save(actor);
            }
        }

        @Test
        void save_directors() {
            Faker faker = new Faker();
            Slugify slugify = Slugify.builder().build();
            for (int i = 0; i < 30; i++) {
                String name = faker.name().fullName();
                Directors director = Directors.builder()
                        .name(name)
                        .slug(slugify.slugify(name))
                        .avatar("https://placehold.co/600x400?text=" + name.substring(0, 1).toUpperCase())
                        .bio(faker.lorem().paragraph())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                directorRepository.save(director);
            }
        }

        @Test
        void save_users() {
            Faker faker = new Faker();
            for (int i = 0; i < 100; i++) {
                String displayName = faker.name().fullName();
                String phoneNumber;
                do {
                    phoneNumber = faker.numerify("09########");
                } while (userRepository.existsByPhone(phoneNumber));

                User user = User.builder()
                        .name(faker.name().username())
                        .displayName(displayName)
                        .email(faker.internet().emailAddress())
                        .avatar("https://placehold.co/600x400?text=" + displayName.substring(0, 1).toUpperCase())
                        .phone(phoneNumber)
                        .password("123")
                        .role(i < 2 ? Role.ADMIN : Role.USERS)
                        .isEnabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                userRepository.save(user);
            }
        }

    @Test
    void update_user_password() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String password = user.getPassword();
            String newPassword = passwordEncoder.encode(password);
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }

        @Test
        void save_posts() {
            Faker faker = new Faker();
            Slugify slugify = Slugify.builder().build();

            List<User> users = userRepository.findByRole(Role.ADMIN);

            for (int i = 0; i < 50; i++) {
                String title = faker.book().title();
                Boolean status = faker.bool().bool();
                User rdUser = users.get(faker.number().numberBetween(0, users.size()));
                Posts post = Posts.builder()
                        .title(title)
                        .slug(slugify.slugify(title))
                        .content(faker.lorem().paragraph(50))
                        .description(faker.lorem().paragraph())
                        .thumbnail("https://placehold.co/600x400?text=" + title.substring(0, 1).toUpperCase())
                        .status(status)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .publishedAt(status ? LocalDateTime.now() : null)
                        .user(rdUser)
                        .build();
                postRepository.save(post);
            }
        }

        @Test
        void save_movies() {
            Faker faker = new Faker();
            Slugify slugify = Slugify.builder().build();
            Random rd = new Random();

            List<Country> countries = countryRepository.findAll();
            List<Genres> genres = genreRepository.findAll();
            List<Actors> actors = actorRepository.findAll();
            List<Directors> directors = directorRepository.findAll();

            for (int i = 0; i < 1000; i++) {
                // Random a country
                Country rdCountry = countries.get(rd.nextInt(countries.size()));

                // Random list genres
                List<Genres> rdGenres = new ArrayList<>();
                // Random 2 -> 4 genres
                for (int j = 0; j < rd.nextInt(3) + 2; j++) {
                    Genres rdGenre = genres.get(rd.nextInt(genres.size()));
                    if (!rdGenres.contains(rdGenre)) {
                        rdGenres.add(rdGenre);
                    }
                }

                // Random list actors
                List<Actors> rdActors = new ArrayList<>();
                // Random 4 -> 7 actors
                for (int j = 0; j < rd.nextInt(4) + 4; j++) {
                    Actors rdActor = actors.get(rd.nextInt(actors.size()));
                    if (!rdActors.contains(rdActor)) {
                        rdActors.add(rdActor);
                    }
                }

                // Random list directors
                List<Directors> rdDirectors = new ArrayList<>();
                // Random 1 -> 2 directors
                for (int j = 0; j < rd.nextInt(2) + 1; j++) {
                    Directors rdDirector = directors.get(rd.nextInt(directors.size()));
                    if (!rdDirectors.contains(rdDirector)) {
                        rdDirectors.add(rdDirector);
                    }
                }

                // Tao entity
                String name = faker.funnyName().name();
                String thumbnail = "https://placehold.co/600x400?text=" + name.substring(0, 1).toUpperCase();
                Boolean status = faker.bool().bool();

                int rdIndexType = rd.nextInt(MovieType.values().length);
                MovieType type = MovieType.values()[rdIndexType];

                Movie movie = Movie.builder()
                        .name(name)
                        .slug(slugify.slugify(name))
                        .description(faker.lorem().paragraph())
                        .thumbnail(thumbnail)
                        .releaseYear(faker.number().numberBetween(1990, 2021))
                        .trailer("https://www.youtube.com/embed/W_0AMP9yO1w?si=JcCeGorHalCHKCPl")
                        .status(status)
                        .rating(faker.number().randomDouble(1, 5, 10))
                        .type(type)
                        .creationDate(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .publishDate(status ? LocalDateTime.now() : null)
                        .country(rdCountry)
                        .genres(rdGenres)
                        .actors(rdActors)
                        .directors(rdDirectors)
                        .build();

                // Luu vao DB
                movieRepository.save(movie);
            }
        }

        @Test
        void save_episodes() {
            Faker faker = new Faker();
            List<Movie> movies = movieRepository.findAll();

            for (Movie movie : movies) {
                if (movie.getType().equals(MovieType.PHIM_BO)) {
                    // random 5 -> 10 episodes
                    for (int i = 0; i < faker.number().numberBetween(5, 11); i++) {
                        Episodes episode = Episodes.builder()
                                .name("Tập " + (i + 1))
                                .duration(faker.number().numberBetween(40, 60))
                                .displayOrder(i + 1)
                                .videoUrl("https://www.youtube.com/embed/n4S5-nRUWbE?si=OlDp1WFbsxzXDtV6")
                                .status(true)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .publishedAt(LocalDateTime.now())
                                .movie(movie)
                                .build();
                        episodeRepository.save(episode);
                    }
                } else {
                    Episodes episode = Episodes.builder()
                            .name("Tập full")
                            .duration(faker.number().numberBetween(90, 120))
                            .displayOrder(1)
                            .videoUrl("https://www.youtube.com/embed/W_0AMP9yO1w?si=JcCeGorHalCHKCPl")
                            .status(true)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .publishedAt(LocalDateTime.now())
                            .movie(movie)
                            .build();
                    episodeRepository.save(episode);
                }
            }
        }

        @Test
        void save_reviews() {
            Faker faker = new Faker();
            Random rd = new Random();

            List<User> users = userRepository.findAll();
            List<Movie> movies = movieRepository.findAll();

            for (Movie movie : movies) {
                // random 10 -> 20 reviews
                for (int i = 0; i < rd.nextInt(11) + 10; i++) {
                    User rdUser = users.get(rd.nextInt(users.size()));
                    Reviews review = Reviews.builder()
                            .content(faker.lorem().paragraph())
                            .rating(rd.nextInt(6) + 5)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .user(rdUser)
                            .movie(movie)
                            .build();
                    reviewRepository.save(review);
                }
            }
        }
    @Test
    void save_favorites() {
        Faker faker = new Faker();
        Random rd = new Random();

        List<User> users = userRepository.findAll();  // Lấy tất cả người dùng từ cơ sở dữ liệu
        List<Movie> movies = movieRepository.findAll();  // Lấy tất cả phim từ cơ sở dữ liệu

        for (User user : users) {
            // Random số lượng favorites từ 5 đến 10 cho mỗi người dùng
            for (int i = 0; i < rd.nextInt(20) + 5; i++) {
                Movie rdMovie = movies.get(rd.nextInt(movies.size()));  // Chọn ngẫu nhiên một bộ phim

                // Sử dụng Faker để tạo ra một ngày ngẫu nhiên trong quá khứ (ví dụ 1-5 năm trước)
                LocalDateTime randomDate = faker.date().past(365 * 5, TimeUnit.DAYS).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();  // Chuyển thành LocalDateTime

                Favorites favorite = Favorites.builder()
                        .createdAt(randomDate)  // Thời gian ngẫu nhiên từ Faker
                        .user(user)  // Người dùng
                        .movie(rdMovie)  // Bộ phim
                        .build();
                favoriteRepository.save(favorite);  // Lưu vào cơ sở dữ liệu
            }
        }
    }

    @Test
        void testQuery() {
            // Movie movie = movieRepository.findByName("Dinah Soares");
            // System.out.println(movie);

            // Sap xep
//        List<Movie> movieSortByRating = movieRepository
//                .findByRatingLessThan(8.0, Sort.by("rating").descending().and(Sort.by("name").ascending()));
//        movieSortByRating.forEach(movie -> System.out.println(movie.getName() + " - " + movie.getRating()));

            // Phan trang
            Pageable pageable = PageRequest.of(0, 5, Sort.by("rating").descending());
            Page<Movie> moviePage = movieRepository.findByNameContaining("a", pageable);
            System.out.println("Total pages: " + moviePage.getTotalPages());
            System.out.println("Total elements: " + moviePage.getTotalElements());
            moviePage.getContent().forEach(movie -> System.out.println(movie.getName() + " - " + movie.getRating()));
        }

    }



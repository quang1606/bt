package com.example.baitapentitymovies;

import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.model.enums.MovieType;
import com.example.baitapentitymovies.repository.MovieRepository;
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
import java.util.List;
import java.util.Random;
@SpringBootTest
class BaitapentitymoviesApplicationTests {
@Autowired
private MovieRepository movieRepository;
    @Test
    void save_movies() {
        Faker faker = new Faker();
        Slugify slg = Slugify.builder().build();
        Random rand = new Random();
        for (int i = 0; i < 150; i++) {
            String name = faker.book().title();
            String thumbnail = "https://placehold.co/600x400?text="+ name.substring(0,1).toLowerCase();
            Boolean status = faker.bool().bool();
            int rdIndexMovieTybe = rand.nextInt(MovieType.values().length);
            MovieType type=MovieType.values()[rdIndexMovieTybe];
            Movie movie = Movie.builder()
                    .name(name)
                    .slug(slg.slugify(name))
                    .description(faker.lorem().word())
                    .thumbnail(thumbnail)
                    .releaseYear(faker.number().numberBetween(1980,2025))
                    .status(status)
                    .trailer("https://www.youtube.com/embed/SwQ7RWsZsus?si=VviYggvXx6YrJcx0")
                    .rating(faker.number().randomDouble(1,5,10))
                    .type(type)
                    .creationDate(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .publishDate(LocalDateTime.now())

                    .build();
            //Luu vao co so su lieu(2 chuc nang cap nhat hoac tap moi)
            movieRepository.save(movie);
        }
    }

    @Test
    void TestQuery(){
//        Movie movie = movieRepository.findByName("The Green Bay Tree");
//
//        System.out.println(movie);

//        List<Movie> movies = movieRepository.findByRatingLessThanOrderByRatingDesc_NQ2(8.0);
//        movies.forEach(System.out::println);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rating").descending());
//        Page<Movie> movies = movieRepository.findByNameContaining("a",pageable);
//        System.out.println("Total number of movies: " + movies.getTotalElements());
//        System.out.println("Total pages: " + movies.getTotalPages());
//        movies.getContent().forEach(movie -> System.out.println(movie.getName()+" - "+movie.getRating()));
        Movie moviePage = movieRepository.findByIdSlugStatus("to-a-god-unknown",1,true);

        System.out.println(moviePage);
    }

}

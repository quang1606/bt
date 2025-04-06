package com.example.baitapentitymovies.controller.web;

import com.example.baitapentitymovies.entity.Episodes;
import com.example.baitapentitymovies.service.EpisodesService;
import org.springframework.ui.Model;

import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.model.enums.MovieType;
import com.example.baitapentitymovies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final MovieService movieService;
    private final EpisodesService episodesService;
    @GetMapping("/")
    public String GetHomePage(Model model ) {
        List<Movie> moviePage = movieService.findByStatus(true,4);
        List<Movie> phimLeList = movieService.findByType(MovieType.PHIM_LE,true,1,6).getContent();
        List<Movie> phimBoList = movieService.findByType(MovieType.PHIM_BO,true,1,6).getContent();
        List<Movie> phimChieuRapList= movieService.findByType(MovieType.PHIM_CHIEU_RAP,true,1,6).getContent();

        model.addAttribute("moviePage", moviePage);
        model.addAttribute("phimLeList", phimLeList);
        model.addAttribute("phimBoList", phimBoList);
        model.addAttribute("phimChieuRapList", phimChieuRapList);
        return "index";
    }
    @GetMapping("/phim-bo")
    public String getPhimBoPage(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "18") Integer pageSize,
                                Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_BO, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "phimbo";
    }
    @GetMapping("/phim-chieu-rap")
    public String getPhimRapPage(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "18") Integer pageSize,
                                Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_CHIEU_RAP, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "phimChieuRap";
    }
    @GetMapping("/phim-le")
    public String getPhimLePage(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "18") Integer pageSize,
                                 Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_LE, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "phimle";
    }

    @GetMapping("/phim/{id}/{slug}")
    public String getMovieDetailsPage(@PathVariable Integer id, @PathVariable String slug, Model model) {
        Movie movieList = movieService.findByIdSlugStatus(id, slug);
        List<Movie> moviePage = movieService.findBySlugAndStatus(movieList.getType(),true,6);
        List<Episodes> episodes =movieService.findEpisodesByMovieTypeSorted(id,true);
        model.addAttribute("movieList", movieList);
        model.addAttribute("movieType", movieList.getType());
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("episodes", episodes);

        return "chitietphim";
    }
    @GetMapping("/xem-phim/{id}/{slug}")
    public String getMovieDetailsPage(@PathVariable Integer id, @PathVariable String slug, Model model,@RequestParam String tap) {
       Movie movie = movieService.findByIdSlugStatus(id, slug);
        model.addAttribute("movie", movie);
        List<Episodes> episodesList=episodesService.findEpisodeByMovieId(id);
        Episodes episodes = episodesService.findEpisodeByDisplayOrder(id,tap);
        List<Movie> moviePage = movieService.findBySlugAndStatus(movie.getType(),true,6);
        model.addAttribute("episodesList", episodesList);
        model.addAttribute("episodes", episodes);
        model.addAttribute("moviePage", moviePage);
        return "xem-phim";
    }

}

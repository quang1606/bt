package com.example.baitapentitymovies.controller.web;

import com.example.baitapentitymovies.entity.Episodes;
import com.example.baitapentitymovies.entity.Posts;
import com.example.baitapentitymovies.service.EpisodesService;
import com.example.baitapentitymovies.service.PostsService;
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
    private final PostsService postsService;
    @GetMapping("/")
    public String GetHomePage(Model model ) {
        List<Movie> moviePage = movieService.findByStatus(true,4);
        List<Movie> phimLeList = movieService.findByType(MovieType.PHIM_LE,true,1,6).getContent();
        List<Movie> phimBoList = movieService.findByType(MovieType.PHIM_BO,true,1,6).getContent();
        List<Movie> phimChieuRapList= movieService.findByType(MovieType.PHIM_CHIEU_RAP,true,1,6).getContent();
        Page<Posts> tinTuc = postsService.finByPost(true,1,4);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("phimLeList", phimLeList);
        model.addAttribute("phimBoList", phimBoList);
        model.addAttribute("tinTuc", tinTuc);
        model.addAttribute("phimChieuRapList", phimChieuRapList);
        return "web/index";
    }
    @GetMapping("/phim-bo")
    public String getPhimBoPage(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "18") Integer pageSize,
                                Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_BO, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "web/phimbo";
    }
    @GetMapping("/phim-chieu-rap")
    public String getPhimRapPage(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "18") Integer pageSize,
                                Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_CHIEU_RAP, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "web/phimChieuRap";
    }
    @GetMapping("/phim-le")
    public String getPhimLePage(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "18") Integer pageSize,
                                 Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_LE, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "web/phimle";
    }

    @GetMapping("/phim/{id}/{slug}")
    public String getMovieDetailsPage(@PathVariable Integer id, @PathVariable String slug, Model model) {
        Movie movieList = movieService.findByIdSlugStatus(id, slug);
        List<Movie> moviePage = movieService.findBySlugAndStatus(movieList.getType(),true,6);
        List<Episodes> episodes =movieService.findEpisodesByMovieTypeSorted(id,true);
        Page<Movie> moviePage1 = movieService.findByTypeAndStatusAndRating(movieList.getType(),true,6);
        model.addAttribute("movieList", movieList);
        model.addAttribute("movieType", movieList.getType());
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("episodes", episodes);
model.addAttribute("moviePage1", moviePage1);
        return "web/chitietphim";
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
        return "web/xem-phim";
    }
    @GetMapping("/phim-yeu-thich")
    public String getFavorites(Model model,@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "18") Integer pageSize) {

        model.addAttribute("currentPage", page);
        return "web/phimyeuthich";
    }
    @GetMapping("/tin-tuc")
    public String getPost(Model model, @RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Posts> postsPage = postsService.finByPost(true,page,pageSize);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        return "web/tintuc";
    }
   @GetMapping("/chi-tiet-tin-tuc/{id}")
    public String getPostDetail(Model model, @PathVariable Integer id , @RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "5") Integer pageSize)  {
        Posts posts = postsService.finByPostStatusAndId(true,id);
        Page<Posts> postsPage = postsService.finByPost(true,page,pageSize);
       model.addAttribute("postsPage", postsPage);
        model.addAttribute("posts", posts);
        return "web/chitiettintuc";

   }

}

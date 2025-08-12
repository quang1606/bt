package com.example.baitapentitymovies.controller.admin;

import com.example.baitapentitymovies.repository.*;
import com.example.baitapentitymovies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/movies")
public class AdminController {
    private final CountryRepository countryRepository;
    private final ActorsRepository actorsRepository;
    private final DirectorsRepository directorsRepository;
    private final GenresRepository genresRepository;
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    @GetMapping()
    public String GetIndexPage() {
        return "admin/movie/index";
    }
    @GetMapping("/create")
    public String GetCreatePage(Model model) {
        model.addAttribute("countries",countryRepository.findAll());
        model.addAttribute("actor",actorsRepository.findAll());
        model.addAttribute("director",directorsRepository.findAll());
        model.addAttribute("genres",genresRepository.findAll());
        return "admin/movie/create";
    }
    @GetMapping("/{id}")
    public String GetDetailPage(@PathVariable int id,Model model) {
        model.addAttribute("movie",movieRepository.findById(id).get());
        model.addAttribute("actor",actorsRepository.findAll());
        model.addAttribute("director",directorsRepository.findAll());
        model.addAttribute("genres",genresRepository.findAll());
        model.addAttribute("countries",countryRepository.findAll());

        return "admin/movie/detail";
    }

}

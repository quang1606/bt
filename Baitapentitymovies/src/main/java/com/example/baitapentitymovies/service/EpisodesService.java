package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.Episodes;
import com.example.baitapentitymovies.repository.EpisodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodesService {
    private final EpisodesRepository episodesRepository;
    public Episodes findEpisodeByDisplayOrder(Integer id, String tap) {
        Integer displayOrder = tap.equals("full")?1:Integer.parseInt(tap);
           return   episodesRepository.findByMovie_IdAndDisplayOrderAndStatus(id,displayOrder,true);

    }

    public List<Episodes> findEpisodeByMovieId(Integer id) {
        List<Episodes> episodesList = episodesRepository.findByMovie_IdAndStatusOrderByDisplayOrderAsc(id,true);
        return episodesList;
    }
}

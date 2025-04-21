package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.Episodes;
import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.exception.BadRequestException;
import com.example.baitapentitymovies.model.enums.Role;
import com.example.baitapentitymovies.model.request.CreateEpisodeRequest;
import com.example.baitapentitymovies.model.request.UpdateEpisodeRequest;
import com.example.baitapentitymovies.repository.EpisodesRepository;
import com.example.baitapentitymovies.repository.MovieRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EpisodesService {
    private final EpisodesRepository episodesRepository;
    private final HttpSession session;
    private final MovieRepository movieRepository;
    public Episodes findEpisodeByDisplayOrder(Integer id, String tap) {
        Integer displayOrder = tap.equals("full")?1:Integer.parseInt(tap);
           return   episodesRepository.findByMovie_IdAndDisplayOrderAndStatus(id,displayOrder,true);

    }

    public List<Episodes> findEpisodeByMovieId(Integer id) {
        List<Episodes> episodesList = episodesRepository.findByMovie_IdAndStatusOrderByDisplayOrderAsc(id,true);
        return episodesList;
    }


    public Page<Episodes> findByMovieId( Integer page, Integer pageSize, Integer movieId) {

        if (movieId == null) {
            throw new BadRequestException("Thiếu movieId");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy phim với ID: " + movieId));

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Episodes> episodesPage = episodesRepository.findByMovie_Id(movieId,pageable);
        return episodesPage;
    }

    public Episodes createEpisodes(CreateEpisodeRequest request) {


        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new BadRequestException("Không tìm thấy phim với ID: " + request.getMovieId()));
        Optional<Episodes> existingEpisodeOpt = episodesRepository.findByMovie_IdAndDisplayOrder(movie.getId(), request.getDisplayOrder());
        if (existingEpisodeOpt.isPresent()) {
            throw new BadRequestException("Thứ tự hiển thị (displayOrder) " + request.getDisplayOrder() + " đã tồn tại cho phim này.");
        }

        Episodes episodes = Episodes.builder()
                .name(request.getName())
                .displayOrder(request.getDisplayOrder())
                .status(request.getStatus())
                .movie(movie)
                .build();

     return    episodesRepository.save(episodes);
    }

    public Episodes updateEpisodes(UpdateEpisodeRequest request, Integer id) {


        // Kiểm tra xem tập phim có tồn tại không
        Episodes episodes = episodesRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy tập phim với ID: " + id));
        Optional<Episodes> existingEpisodeOpt = episodesRepository.findByMovie_IdAndDisplayOrder(episodes.getMovie().getId(), request.getDisplayOrder());
        if (existingEpisodeOpt.isPresent()) {
            throw new BadRequestException("Thứ tự hiển thị (displayOrder) " + request.getDisplayOrder() + " đã tồn tại cho phim này.");
        }
        // Cập nhật thông tin tập phim từ request
        episodes.setName(request.getName());
        episodes.setDisplayOrder(request.getDisplayOrder());
        episodes.setStatus(request.getStatus());

        return episodesRepository.save(episodes);
    }


    public void daleteEpisodes(Integer id) {

        Episodes episodes = episodesRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy tập phim với ID: " + id));
        episodesRepository.delete(episodes);
    }
}

package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.Favorites;
import com.example.baitapentitymovies.entity.Movie;
import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.exception.BadRequestException; // Giữ lại nếu cần
import com.example.baitapentitymovies.repository.FavoritesRepository;
import com.example.baitapentitymovies.repository.MovieRepository;
import com.example.baitapentitymovies.repository.UserRepository; // Vẫn có thể cần cho các mục đích khác
import jakarta.servlet.http.HttpServletRequest; // THÊM IMPORT NÀY
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder; // Cách khác để lấy request
import org.springframework.web.context.request.ServletRequestAttributes; // Cách khác để lấy request


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavouriteService {
    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository; // Vẫn giữ lại nếu bạn cần tìm User theo cách khác
    private final MovieRepository movieRepository;
    // private final HttpSession session; // XÓA DÒNG NÀY
    private final HttpServletRequest request; // THÊM DÒNG NÀY ĐỂ INJECT REQUEST

    // Helper method để lấy User hiện tại từ request attribute
    private User getCurrentUser() {
        User user = (User) request.getAttribute("currentUser");
        if (user == null) {
            // Điều này không nên xảy ra nếu JwtAuthenticationInterceptor hoạt động đúng
            // và API này được bảo vệ bởi interceptor đó.
            throw new BadRequestException("Không tìm thấy thông tin người dùng đã xác thực.");
        }
        return user;
    }

    public Page<Movie> getFavouriteMovie(Integer page, Integer pageSize) {
        User user = getCurrentUser(); // Lấy user từ request attribute

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending());
        // Giả sử bạn có phương thức findMoviesByUser_Id trong FavoritesRepository
        // Hoặc nếu bạn muốn lấy trực tiếp từ Favorites, bạn có thể cần map
        return favoritesRepository.findMoviesByUserId(user.getId(), pageable);
    }

    public Favorites postFavouriteMovie(int movieId) { // Sửa int id thành int movieId cho rõ ràng
        User user = getCurrentUser();

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy phim với ID: " + movieId));

        // Kiểm tra xem phim đã được yêu thích chưa để tránh trùng lặp (tùy chọn)
        if (favoritesRepository.existsByMovie_IdAndUser_Id(movieId, user.getId())) {
            throw new BadRequestException("Phim này đã có trong danh sách yêu thích của bạn.");
        }

        Favorites favorites = Favorites.builder()
                .createdAt(LocalDateTime.now())
                .movie(movie)
                .user(user)
                .build();
        return favoritesRepository.save(favorites);
    }

    public void deleteFavouriteMovie(int movieId) { // Sửa int id thành int movieId
        User user = getCurrentUser();

        // Tìm đối tượng Favorites cụ thể để xóa
        Favorites favoriteEntry = favoritesRepository.findFavoritesByMovie_IdAndUser_Id(movieId, user.getId())
                .orElseThrow(() -> new BadRequestException("Phim không có trong danh sách yêu thích hoặc không tìm thấy."));

        // Kiểm tra quyền (mặc dù user.getId() đã được dùng để tìm, kiểm tra lại cho chắc)
        // if (!favoriteEntry.getUser().getId().equals(user.getId())) {
        //     throw new UnauthorizedException("Không có quyền xóa mục yêu thích này.");
        // }
        // Dòng trên thường không cần thiết nếu query đã lọc theo user.getId()

        favoritesRepository.delete(favoriteEntry);
    }

    public void deleteAllFavouriteMovie() {
        User user = getCurrentUser();

        List<Favorites> favoritesList = favoritesRepository.findByUserId(user.getId()); // Đảm bảo tên phương thức đúng
        if (favoritesList.isEmpty()) {
            // Không có gì để xóa, có thể trả về hoặc không làm gì cả
            return;
        }
        // Không cần kiểm tra quyền phức tạp ở đây nữa vì đã lấy theo user.getId()
        favoritesRepository.deleteAll(favoritesList);
    }

    public boolean isFavourite(int movieId) {
        User user = getCurrentUser();
        // Không cần lấy user từ DB nữa vì đã có từ request
        return favoritesRepository.existsByMovie_IdAndUser_Id(movieId, user.getId());
    }
}
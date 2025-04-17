package com.example.baitapentitymovies.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateEpisodeRequest {
    @NotBlank(message = "Tên tập phim không được để trống")
    private String name;

    @NotNull(message = "Thứ tự hiển thị không được để trống")
    private Integer displayOrder;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean status;

    @NotNull(message = "MovieId không được để trống")
    private Integer movieId;
}

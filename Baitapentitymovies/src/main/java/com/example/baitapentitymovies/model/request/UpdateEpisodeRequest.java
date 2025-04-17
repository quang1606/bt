package com.example.baitapentitymovies.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateEpisodeRequest {

    @NotEmpty(message = "Tên tập phim không được để trống")
    private String name;

    @NotNull(message = "Thứ tự hiển thị không được để trống")
    private Integer displayOrder;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean status;
}

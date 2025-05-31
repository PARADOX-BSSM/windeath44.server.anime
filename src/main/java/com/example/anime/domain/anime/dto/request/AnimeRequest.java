package com.example.anime.domain.anime.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AnimeRequest(
        @NotNull(message="name is null")
        String name,
        String description,
        @NotNull(message="start year is null")
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
        LocalDate start_year,
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
        LocalDate end_year,
        @NotNull(message="tag is null")
        List<String> tags,
        @NotNull(message="imageUrl is null")
        String imageUrl
) {
}

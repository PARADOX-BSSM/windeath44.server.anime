package com.example.anime.domain.anime.dto.response;

import java.time.LocalDate;
import java.util.List;

public record AnimeListResponse(
        Long animeId,
        String name,
        String description,
        LocalDate start_year,
        LocalDate end_year,
        Long airDates,
        List<String> tags,
        Long bow_count,
        String image_url
) {
}
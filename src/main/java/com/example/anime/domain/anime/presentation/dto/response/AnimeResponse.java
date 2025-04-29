package com.example.anime.domain.anime.presentation.dto.response;


import java.time.LocalDate;
import java.util.List;

public record AnimeResponse(
        String name,
        String description,
        LocalDate start_year,
        LocalDate end_year,
        Long airDates,
        List<String> tags,
        Long bow_count
) {

}

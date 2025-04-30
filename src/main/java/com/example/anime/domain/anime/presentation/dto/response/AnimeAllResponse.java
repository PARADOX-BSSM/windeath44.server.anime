package com.example.anime.domain.anime.presentation.dto.response;

import com.example.anime.domain.character.domain.Character;

import java.time.LocalDate;
import java.util.List;

public record AnimeAllResponse (
        Long animeId,
        String name,
        String description,
        LocalDate start_year,
        LocalDate end_year,
        Long airDates,
        List<String> tags,
        Long bow_count
) {
}

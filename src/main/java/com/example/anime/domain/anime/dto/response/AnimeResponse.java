package com.example.anime.domain.anime.dto.response;

import com.example.anime.domain.character.dto.response.CharacterResponse;

import java.time.LocalDate;
import java.util.List;

public record AnimeResponse(
        String name,
        String description,
        LocalDate start_year,
        LocalDate end_year,
        Long airDates,
        List<String> tags,
        Long bow_count,
        List<CharacterResponse> characters,
        String image_url
) {
}
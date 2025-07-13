package com.example.anime.domain.anime.dto.response;

import java.util.List;

public record RestAnimeResponse (
        Long id,
        String name,
        List<String> genres,
        String img
) {
}

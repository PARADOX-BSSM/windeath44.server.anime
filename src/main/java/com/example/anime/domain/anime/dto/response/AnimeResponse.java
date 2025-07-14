package com.example.anime.domain.anime.dto.response;

import java.util.List;

public record AnimeResponse(
        Long animeId,
        String name,
        List<String> genres,
        String imageUrl
) {
}

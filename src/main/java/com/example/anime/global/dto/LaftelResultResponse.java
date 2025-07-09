package com.example.anime.global.dto;

import com.example.anime.domain.anime.dto.response.RestAnimeResponse;

import java.util.List;

public record LaftelResultResponse (
        Long count,
        List<RestAnimeResponse> results,
        String next
) {
}

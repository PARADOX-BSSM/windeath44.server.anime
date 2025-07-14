package com.example.anime.domain.character.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CharacterResponse (
        Long characterId,
        Long animeId,
        String name,
        Integer age,
        Long lifeTime,
        String deathReason,
        String imageUrl,
        LocalDateTime deathOfDay,
        Long bowCount,
        String state,
        String saying
) {

}
package com.example.anime.domain.character.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CharacterResponse (
        Long characterId,
        String name,
        Integer age,
        Long lifeTime,
        String deathReason,
        String imageUrl,
        LocalDateTime death_of_day,
        Long bow_count
) {

}
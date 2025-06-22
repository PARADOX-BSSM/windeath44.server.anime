package com.example.anime.domain.character.dto.response;

public record CharacterResponse (
        Long characterId,
        String name,
        String content,
        Long lifeTime,
        String deathReason,
        String imageUrl,
        Long bow_count
) {

}
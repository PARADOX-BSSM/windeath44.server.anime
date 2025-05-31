package com.example.anime.domain.character.dto.request;

public record CharacterRequest (
        Long animeId,
        String name,
        String content,
        Long lifeTime,
        String deathReason
) {

}
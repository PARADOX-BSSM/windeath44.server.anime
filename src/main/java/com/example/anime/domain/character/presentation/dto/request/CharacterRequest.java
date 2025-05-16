package com.example.anime.domain.character.presentation.dto.request;

public record CharacterRequest (
        Long animeId,
        String name,
        String content,
        Long lifeTime,
        String deathReason
) {

}
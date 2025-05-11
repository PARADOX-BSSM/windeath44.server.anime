package com.example.anime.domain.character.domain.dto.response;

public record CharacterResponse (
        Long characterId,
        String name,
        String content,
        Long lifeTime,
        String death_reason
) {

}

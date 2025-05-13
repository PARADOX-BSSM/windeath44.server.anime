package com.example.anime.domain.character.presentation.dto.request;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.character.domain.CharacterState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record CharacterRequest (
        Long animeId,
        String name,
        String content,
        Long lifeTime,
        String death_reason
) {

}
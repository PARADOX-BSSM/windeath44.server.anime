package com.example.anime.domain.character.dto.response;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record CharacterResponse (
        Long characterId,
        Long animeId,
        String name,
        Integer age,
        Long lifeTime,
        String deathReason,
        String imageUrl,
        LocalDate deathOfDay,
        Long bowCount,
        String state,
        String saying
) {

}
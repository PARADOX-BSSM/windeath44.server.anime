package com.example.anime.domain.character.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CharacterRequest (
        @NotNull(message="animeId is null")
        Long animeId,
        @NotNull(message="name is null")
        String name,
        @NotNull(message="age is null")
        Integer age,
        @NotNull(message="lifeTime is null")
        Long lifeTime,
        @NotNull(message="deathReason is null")
        String deathReason,
        @JsonFormat(pattern = "yy-mm-dd", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
        LocalDateTime deathOfDay
) {

}
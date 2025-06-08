package com.example.anime.domain.character.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public record CharacterRequest (
        Long animeId,
        String name,
        String content,
        Long lifeTime,
        String deathReason,
        MultipartFile image
) {

}
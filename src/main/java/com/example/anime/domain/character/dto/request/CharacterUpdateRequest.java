package com.example.anime.domain.character.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public record CharacterUpdateRequest (
        String name,
        String content,
        Long lifeTime,
        String deathReason,
        MultipartFile image
) {

}
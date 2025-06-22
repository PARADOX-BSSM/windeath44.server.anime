package com.example.anime.domain.character.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record CharacterUpdateRequest (
        String name,
        String content,
        Long lifeTime,
        String deathReason,
        MultipartFile image
) {

}
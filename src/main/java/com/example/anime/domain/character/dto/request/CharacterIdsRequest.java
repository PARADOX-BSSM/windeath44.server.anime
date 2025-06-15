package com.example.anime.domain.character.dto.request;

import java.util.List;

public record CharacterIdsRequest (
        List<Long> characterIds
) {
}

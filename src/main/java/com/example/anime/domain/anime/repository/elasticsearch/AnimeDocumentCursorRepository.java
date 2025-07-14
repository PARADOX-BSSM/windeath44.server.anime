package com.example.anime.domain.anime.repository.elasticsearch;

import com.example.anime.domain.anime.model.AnimeDocument;
import com.example.anime.global.dto.DocumentSlice;

public interface AnimeDocumentCursorRepository {
    DocumentSlice<AnimeDocument> findAnimesByName(int size, String animeName);

    DocumentSlice<AnimeDocument> findAnimesByCursorIdAndName(Long cursorId, int size, String animeName);
}

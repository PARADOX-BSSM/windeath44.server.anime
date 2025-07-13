package com.example.anime.domain.anime.repository;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.model.AnimeDocument;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.DocumentSlice;
import org.springframework.data.domain.Slice;

public interface AnimeDocumentCursorRepository {
    DocumentSlice<AnimeDocument> findAnimesByName(int size, String animeName);

    DocumentSlice<AnimeDocument> findAnimesByCursorIdAndName(Long cursorId, int size, String animeName);
}

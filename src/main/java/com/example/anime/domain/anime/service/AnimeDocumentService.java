package com.example.anime.domain.anime.service;

import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.mapper.AnimeDocumentMapper;
import com.example.anime.domain.anime.model.AnimeDocument;
import com.example.anime.domain.anime.repository.elasticsearch.AnimeDocumentRepository;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.DocumentSlice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeDocumentService {
    private final AnimeDocumentRepository animeDocumentRepository;
    private final AnimeDocumentMapper animeDocumentMapper;

    public CursorPage<AnimeResponse> findAllByName(String animeName, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        DocumentSlice<AnimeDocument> animeDocumentSlice = cursorId == null
                ? animeDocumentRepository.findAnimesByName(size, animeName)
                : animeDocumentRepository.findAnimesByCursorIdAndName(cursorId, size, animeName);

        List<AnimeResponse> animeResponseList = animeDocumentMapper.toAnimePageListResponse(animeDocumentSlice);
        return new CursorPage<>(animeResponseList, animeDocumentSlice.hasNext());
    }
}

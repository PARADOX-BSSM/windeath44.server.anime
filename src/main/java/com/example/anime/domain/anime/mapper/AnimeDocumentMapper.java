package com.example.anime.domain.anime.mapper;

import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.model.AnimeDocument;
import com.example.anime.global.dto.DocumentSlice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimeDocumentMapper {

    public List<AnimeResponse> toAnimePageListResponse(DocumentSlice<AnimeDocument> animeDocumentSlice) {
        return animeDocumentSlice.getContent()
                .stream()
                .map(this::toAnimeResponse)
                .toList();
    }

    public AnimeResponse toAnimeResponse(AnimeDocument animeDocument) {
        Long animeId = animeDocument.getId();
        String name = animeDocument.getName();
        List<String> tags = animeDocument.getGenres();
        String imageUrl = animeDocument.getImageUrl();
        return new AnimeResponse(animeId, name, tags, imageUrl);
    }
}

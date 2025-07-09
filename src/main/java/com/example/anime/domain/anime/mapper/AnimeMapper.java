package com.example.anime.domain.anime.mapper;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.dto.response.AnimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimeMapper {

  private AnimeResponse toAnimeResponse(Anime anime) {
    Long animeId = anime.getAnimeId();
    String name = anime.getName();
    List<String> tags = anime.getTags();
    String imageUrl = anime.getImageUrl();
    return new AnimeResponse(animeId, name, tags, imageUrl);
  }

  public List<AnimeResponse> toAnimePageListResponse(Slice<Anime> animeSlice) {
    return animeSlice.getContent()
            .stream()
            .map(this::toAnimeResponse)
            .toList();
  }
}

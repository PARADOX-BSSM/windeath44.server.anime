package com.example.anime.domain.anime.mapper;

import com.example.anime.domain.anime.dto.response.RestAnimeResponse;
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
    List<String> tags = anime.getGenres();
    String imageUrl = anime.getImageUrl();
    return new AnimeResponse(animeId, name, tags, imageUrl);
  }

  public List<AnimeResponse> toAnimePageListResponse(Slice<Anime> animeSlice) {
    return animeSlice.getContent()
            .stream()
            .map(this::toAnimeResponse)
            .toList();
  }

  public Anime toAnime(RestAnimeResponse animeResponse) {
    Long animeId = animeResponse.id();
    String name = animeResponse.name();
    String imageUrl = animeResponse.img();
    List<String> genres = animeResponse.genres();

    return Anime.builder()
            .animeId(animeId)
            .name(name)
            .imageUrl(imageUrl)
            .genres(genres)
            .build();
  }
}

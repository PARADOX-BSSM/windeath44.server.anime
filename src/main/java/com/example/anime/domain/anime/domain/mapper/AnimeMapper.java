package com.example.anime.domain.anime.domain.mapper;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.domain.AnimeAirDates;
import com.example.anime.domain.anime.presentation.dto.response.AnimeListResponse;
import com.example.anime.domain.anime.presentation.dto.response.AnimeResponse;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.mapper.CharacterMapper;
import com.example.anime.domain.character.presentation.dto.response.CharacterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimeMapper {

  private final CharacterMapper characterMapper;

  public Anime toAnime(String name, String description, AnimeAirDates airDates, List<String> tags) {
    return Anime.builder()
            .name(name)
            .description(description)
            .airDates(airDates)
            .tags(tags)
            .build();
  }

  public AnimeResponse toAnimeResponse(Anime anime, List<Character> characters) {
    String name = anime.getName();
    String description = anime.getDescription();
    LocalDate startYear = anime.getAirDates().getStartYear();
    LocalDate endYear = anime.getAirDates().getEndYear();
    Long airDates = anime.getAirDates().getAirDay();

    List<String> tags = anime.getTags();
    Long bowCount = anime.getBowCount();

    List<CharacterResponse> characterListResponses = characters.stream()
            .map(characterMapper::toCharacterResponse)
            .toList();
    String imageUrl = anime.getImageUrl();


    return new AnimeResponse(name, description, startYear, endYear, airDates,  tags, bowCount, characterListResponses, imageUrl);
  }

  public AnimeListResponse toAnimeAllResponse(Anime anime) {
    Long animeId = anime.getAnimeId();
    String name = anime.getName();
    String description = anime.getDescription();
    List<String> tags = anime.getTags();
    LocalDate startYear = anime.getAirDates().getStartYear();
    LocalDate endYear = anime.getAirDates().getEndYear();
    Long airDates = anime.getAirDates().getAirDay();
    Long bowCount = anime.getBowCount();
    String imageUrl = anime.getImageUrl();

    return new AnimeListResponse(animeId, name, description, startYear, endYear, airDates, tags, bowCount, imageUrl);
  }
}

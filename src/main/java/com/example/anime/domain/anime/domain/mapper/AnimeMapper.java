package com.example.anime.domain.anime.domain.mapper;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.domain.AnimeAirDates;
import com.example.anime.domain.anime.presentation.dto.response.AnimeListResponse;
import com.example.anime.domain.anime.presentation.dto.response.AnimeResponse;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.mapper.CharacterMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel="spring", builder = @Builder(disableBuilder = false), uses=CharacterMapper.class)
public interface AnimeMapper {

  @Mapping(target = "animeId", ignore = true)
  @Mapping(target = "bow_count", ignore = true)
  Anime toAnime(String name, String description, AnimeAirDates airDates, List<String> tags);

  @Mapping(source = "anime.airDates.start_year", target = "start_year")
  @Mapping(source = "anime.airDates.end_year", target = "end_year")
  AnimeResponse toAnimeResponse(Anime anime, List<Character> characters);

  @Mapping(source = "airDates.start_year", target = "start_year")
  @Mapping(source = "airDates.end_year", target = "end_year")
  AnimeListResponse toAnimeAllResponse(Anime anime);

  default Long getAirDates(AnimeAirDates airDates) {
    return airDates.getAirDay();
  }

}

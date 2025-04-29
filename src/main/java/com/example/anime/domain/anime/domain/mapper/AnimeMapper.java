package com.example.anime.domain.anime.domain.mapper;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.domain.AnimeAirDates;
import com.example.anime.domain.anime.presentation.dto.response.AnimeResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel="spring", builder = @Builder(disableBuilder = false))
public interface AnimeMapper {

  @Mapping(target = "animeId", ignore = true)
  @Mapping(target = "bow_count", ignore = true)
  Anime toEntity(String name, String description, AnimeAirDates airDates, List<String> tags);

  @Mapping(source = "airDates.start_year", target = "start_year")
  @Mapping(source = "airDates.end_year", target = "end_year")
  AnimeResponse toDto(Anime anime);

  default Long getAirDates(AnimeAirDates airDates) {
    return airDates.getAirDay();
  }

}

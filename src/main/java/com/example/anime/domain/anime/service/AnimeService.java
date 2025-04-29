package com.example.anime.domain.anime.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.domain.AnimeAirDates;
import com.example.anime.domain.anime.domain.mapper.AnimeMapper;
import com.example.anime.domain.anime.domain.repository.AnimeRepository;
import com.example.anime.domain.anime.presentation.dto.response.AnimeResponse;
import com.example.anime.domain.anime.service.exception.NotFoundAnimation;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
  private final AnimeRepository animeRepository;
  private final AnimeMapper animeMapper;

  @Transactional
  public void create(String name, String description, LocalDate start_year, LocalDate end_year, List<String> tags) {
    Anime anime = createAnime(name, description, start_year, end_year, tags);
    animeRepository.save(anime);
  }


  private Anime createAnime(String name, String description, LocalDate start_year, LocalDate end_year, List<String> tags) {
    AnimeAirDates animeAirDates = AnimeAirDates.builder()
            .start_year(start_year)
            .end_year(end_year)
            .build();
    return animeMapper.toEntity(name, description, animeAirDates, tags);
  }

  public void delete(Long animeId) {
    Anime anime = findAnime(animeId);
    animeRepository.delete(anime);
  }

  private Anime findAnime(Long animeId) {
    Anime anime = animeRepository.findByIdWithTags(animeId)
            .orElseThrow(() -> new NotFoundAnimation("Not found Animation with Id"));
    return anime;
  }

  public List<AnimeResponse> findAll() {
    List<AnimeResponse> animeList = animeRepository.findAllWithTags()
            .stream()
            .map(animeMapper::toDto)
            .toList();

    return animeList;
  }

  public AnimeResponse findById(Long animeId) {
    Anime anime = findAnime(animeId);
    AnimeResponse animeResponse = animeMapper.toDto(anime);
    return animeResponse;
  }

  @Transactional
  public void update(Long animeId, String name, String description ,LocalDate start_year, LocalDate end_year, List<String> tags) {
    Anime anime = findAnime(animeId);
    anime.update(name, description, start_year, end_year, tags);
    animeRepository.save(anime);
  }
}

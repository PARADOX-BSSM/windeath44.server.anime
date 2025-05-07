package com.example.anime.domain.anime.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.domain.AnimeAirDates;
import com.example.anime.domain.anime.domain.mapper.AnimeMapper;
import com.example.anime.domain.anime.domain.repository.AnimeRepository;
import com.example.anime.domain.anime.presentation.dto.response.AnimeAllResponse;
import com.example.anime.domain.anime.presentation.dto.response.AnimeResponse;
import com.example.anime.domain.anime.service.exception.NotFoundAnimeException;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.repository.CharacterRepository;
import com.example.anime.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimeService {
  private final AnimeRepository animeRepository;
  private final AnimeMapper animeMapper;
  private final CharacterService characterService;

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
    return animeMapper.toAnime(name, description, animeAirDates, tags);
  }

  public void delete(Long animeId) {
    Anime anime = findAnime(animeId);
    animeRepository.delete(anime);
  }

  public Anime getAnime(Long animeId) {
    return findAnime(animeId);
  }

    private Anime findAnime(Long animeId) {
    Anime anime = animeRepository.findByIdWithTags(animeId)
            .orElseThrow(NotFoundAnimeException::getInstance);
    return anime;
  }

  public List<AnimeAllResponse> findAll() {
    List<AnimeAllResponse> animeList = animeRepository.findAllWithTags()
            .stream()
            .map(animeMapper::toAnimeAllResponse)
            .toList();
    return animeList;
  }

  public AnimeResponse findById(Long animeId) {
    Anime anime = findAnime(animeId);
    List<Character> characterList = characterService.findAllByAnime(anime);
    AnimeResponse animeResponse = animeMapper.toAnimeResponse(anime, characterList);
    return animeResponse;
  }

  @Transactional
  public void update(Long animeId, String name, String description ,LocalDate start_year, LocalDate end_year, List<String> tags) {
    Anime anime = findAnime(animeId);
    anime.update(name, description, start_year, end_year, tags);
  }
}
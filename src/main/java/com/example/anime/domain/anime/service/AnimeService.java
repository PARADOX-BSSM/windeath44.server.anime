package com.example.anime.domain.anime.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.domain.AnimeAirDates;
import com.example.anime.domain.anime.domain.mapper.AnimeMapper;
import com.example.anime.domain.anime.domain.repository.AnimeRepository;
import com.example.anime.domain.anime.presentation.dto.response.AnimeListResponse;
import com.example.anime.domain.anime.presentation.dto.response.AnimeResponse;
import com.example.anime.domain.anime.service.exception.NotFoundAnimeException;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.mapper.ResponseMapper;
import com.example.anime.global.mapper.dto.CursorPage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
            .startYear(start_year)
            .endYear(end_year)
            .build();
    return animeMapper.toAnime(name, description, animeAirDates, tags);
  }

  @Transactional
  public void delete(Long animeId) {
    Anime anime = findAndRenewAnimeById(animeId);
    animeRepository.delete(anime);
  }

  public Anime getAnime(Long animeId) {
    return findAndRenewAnimeById(animeId);
  }

  @Transactional
  public Anime findAndRenewAnimeById(Long animeId) {
      Anime anime = findAnime(animeId);
      anime.renewalBowCount();
      return anime;
    }

    private Anime findAnime(Long animeId) {
    Anime anime = animeRepository.findByIdWithTagsAAndCharacterList(animeId)
            .orElseThrow(NotFoundAnimeException::getInstance);
    return anime;
  }

  @Transactional
  public List<AnimeListResponse> findAll() {
    List<AnimeListResponse> animeList = animeRepository.findAllWithTagsAndCharacterList()
            .stream()
            .map( anime -> {
                anime.renewalBowCount();
                AnimeListResponse animeListResponse = animeMapper.toAnimeListResponse(anime);
                return animeListResponse;
            } )
            .toList();
    return animeList;
  }

  public AnimeResponse findById(Long animeId) {
    Anime anime = findAndRenewAnimeById(animeId);
    List<Character> characterList = characterService.findAllByAnime(anime);
    AnimeResponse animeResponse = animeMapper.toAnimeResponse(anime, characterList);
    return animeResponse;
  }

  @Transactional
  public void update(Long animeId, String name, String description ,LocalDate start_year, LocalDate end_year, List<String> tags) {
    Anime anime = findAnime(animeId);
    anime.update(name, description, start_year, end_year, tags);
  }

  public CursorPage<AnimeListResponse> findAllByCursorId(Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size + 1);

    // null일 경우 첫 페이지 조회하는거임
    Slice<Anime> animeSlice = cursorId == null
            ? animeRepository.findPage(pageable)
            : animeRepository.findPageByCursorId(cursorId, pageable);

    List<AnimeListResponse> animeList = animeMapper.toAnimePageableListResponse(animeSlice);
    return new CursorPage<>(animeList, animeSlice.hasNext());
  }

}
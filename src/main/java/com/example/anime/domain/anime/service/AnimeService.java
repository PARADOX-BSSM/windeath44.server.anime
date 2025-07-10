package com.example.anime.domain.anime.service;

import com.example.anime.domain.anime.dto.response.RestAnimeResponse;
import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.mapper.AnimeMapper;
import com.example.anime.domain.anime.repository.AnimeRepository;
import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.exception.NotFoundAnimeException;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.LaftelResultResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AnimeService {
  private final AnimeRepository animeRepository;
  private final AnimeMapper animeMapper;

  public CursorPage<AnimeResponse> findAllByCursorId(Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size + 1);

    Slice<Anime> animeSlice = cursorId == null
            ? animeRepository.findRecentAnimes(pageable)
            : animeRepository.findRecentAnimesByCursorId(cursorId, pageable);

    List<AnimeResponse> animeList = animeMapper.toAnimePageListResponse(animeSlice);
    return new CursorPage<>(animeList, animeSlice.hasNext());
  }

  private Anime findAnime(Long animeId) {
    Anime anime = animeRepository.findById(animeId)
            .orElseThrow(NotFoundAnimeException::getInstance);
    return anime;
  }

  public Anime getAnime(Long animeId) {
    Anime anime = findAnime(animeId);
    return anime;
  }

  @Transactional(readOnly = false)
  public void delete(Long animeId) {
    Anime anime = findAnime(animeId);
    animeRepository.delete(anime);
  }

  @Transactional(readOnly = false)
  public void save(LaftelResultResponse animeResponse) {
    List<Anime> animeList = animeResponse.results()
            .stream()
            .map(animeMapper::toAnime)
            .toList();
    animeRepository.bulkInsert(animeList);
  }
}

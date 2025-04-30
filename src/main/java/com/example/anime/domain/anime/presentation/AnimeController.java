package com.example.anime.domain.anime.presentation;

import com.example.anime.domain.anime.presentation.dto.request.AnimeRequest;
import com.example.anime.domain.anime.presentation.dto.response.AnimeAllResponse;
import com.example.anime.domain.anime.presentation.dto.response.AnimeResponse;
import com.example.anime.domain.anime.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animes")
@RequiredArgsConstructor
public class AnimeController {
  private final AnimeService animeService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody @Valid AnimeRequest request) {
    animeService.create(request.name(), request.description(), request.start_year(), request.end_year(), request.tags());
  }

  @GetMapping
  public ResponseEntity<List<AnimeAllResponse>> findAll() {
    List<AnimeAllResponse> animeList = animeService.findAll();
    return ResponseEntity.ok(animeList);
  }

  @GetMapping("/{anime-id}")
  public ResponseEntity<AnimeResponse> findById(@PathVariable("anime-id") Long animeId) {
    AnimeResponse anime = animeService.findById(animeId);
    return ResponseEntity.ok(anime);
  }

  @PatchMapping("/{anime-id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateAnimeInfo(@PathVariable("anime-id") Long animeId, @RequestBody @Valid AnimeRequest request) {
    animeService.update(animeId, request.name(), request.description(), request.start_year(), request.end_year(), request.tags());
  }

  @DeleteMapping("/{anime-id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("anime-id") Long animeId) {
    animeService.delete(animeId);
  }


}

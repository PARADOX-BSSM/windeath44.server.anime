package com.example.anime.domain.anime.controller;

import com.example.anime.domain.anime.dto.request.AnimeRequest;
import com.example.anime.domain.anime.dto.response.AnimeListResponse;
import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.global.mapper.ResponseMapper;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.ResponseDto;
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
  private final ResponseMapper responseMapper;

  @PostMapping
  public ResponseEntity<ResponseDto<Void>> create(@RequestBody @Valid AnimeRequest request) {
    animeService.create(request.name(), request.description(), request.start_year(), request.end_year(), request.tags());
    ResponseDto<Void> responseDto = responseMapper.toResponseDto("create anime", null);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto);
  }

  @GetMapping("/details")
  public ResponseEntity<ResponseDto<CursorPage<AnimeListResponse>>> findAllByCursorId(@RequestParam(value = "cursor-id", required = false) Long cursorId, @RequestParam("size") int size) {
    CursorPage<AnimeListResponse> animeList = animeService.findAllByCursorId(cursorId, size);
    ResponseDto<CursorPage<AnimeListResponse>> responseDto = responseMapper.toResponseDto("find animes with cursorId", animeList);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping
  public ResponseEntity<ResponseDto<List<AnimeListResponse>>> findAll() {
    List<AnimeListResponse> animeList = animeService.findAll();
    ResponseDto<List<AnimeListResponse>> responseDto = responseMapper.toResponseDto("find animes", animeList);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{anime-id}")
  public ResponseEntity<ResponseDto<AnimeResponse>> findById(@PathVariable("anime-id") Long animeId) {
    AnimeResponse anime = animeService.findById(animeId);
    ResponseDto<AnimeResponse> responseDto = responseMapper.toResponseDto("find anime", anime);
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping("/{anime-id}")
  public ResponseEntity<ResponseDto<Void>> updateAnimeInfo(@PathVariable("anime-id") Long animeId, @RequestBody @Valid AnimeRequest request) {
    animeService.update(animeId, request.name(), request.description(), request.start_year(), request.end_year(), request.tags());
    ResponseDto<Void> responseDto = responseMapper.toResponseDto("update anime", null);
    return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(responseDto);
  }

  @DeleteMapping("/{anime-id}")
  public ResponseEntity<ResponseDto<Void>> delete(@PathVariable("anime-id") Long animeId) {
    animeService.delete(animeId);
    ResponseDto<Void> responseDto = responseMapper.toResponseDto("delete anime", null);
    return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(responseDto);
  }
}

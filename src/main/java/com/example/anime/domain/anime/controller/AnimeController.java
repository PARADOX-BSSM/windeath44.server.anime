package com.example.anime.domain.anime.controller;

import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.global.mapper.ResponseMapper;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animes")
@RequiredArgsConstructor
public class AnimeController {
  private final AnimeService animeService;
  private final ResponseMapper responseMapper;

  @GetMapping
  public ResponseEntity<ResponseDto<CursorPage<AnimeResponse>>> findAllByCursorId(@RequestParam(value = "cursor-id", required = false) Long cursorId, @RequestParam("size") int size) {
    CursorPage<AnimeResponse> animeList = animeService.findAllByCursorId(cursorId, size);
    ResponseDto<CursorPage<AnimeResponse>> responseDto = responseMapper.toResponseDto("find animes with cursorId", animeList);
    return ResponseEntity.ok(responseDto);
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

package com.example.anime.domain.anime.controller;

import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.ResponseDto;
import com.example.anime.global.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animes")
@RequiredArgsConstructor
public class AnimeController {
  private final AnimeService animeService;

  @GetMapping
  public ResponseEntity<ResponseDto<CursorPage<AnimeResponse>>> findAll(@RequestParam(value = "cursor-id", required = false) Long cursorId, @RequestParam("size") int size) {
    CursorPage<AnimeResponse> animeList = animeService.findAllByCursorId(cursorId, size);
    ResponseDto<CursorPage<AnimeResponse>> responseDto = HttpUtil.success("find animes with cursorId", animeList);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search")
  public ResponseEntity<ResponseDto<CursorPage<AnimeResponse>>> findAllByAnimeName(@RequestParam("anime") String animeName, @RequestParam(value = "cursor-id", required = false) Long cursorId, @RequestParam("size") int size) {
    CursorPage<AnimeResponse> animeList = animeService.findAllByName(animeName, cursorId, size);
    ResponseDto<CursorPage<AnimeResponse>> responseDto = HttpUtil.success("find animes with name", animeList);
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{anime-id}")
  public ResponseEntity<ResponseDto<Void>> delete(@PathVariable("anime-id") Long animeId) {
    animeService.delete(animeId);
    ResponseDto<Void> responseDto = HttpUtil.success("delete anime");
    return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(responseDto);
  }
}

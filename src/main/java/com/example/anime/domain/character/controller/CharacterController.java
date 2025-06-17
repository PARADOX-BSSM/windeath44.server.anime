package com.example.anime.domain.character.controller;

import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.domain.character.service.usecase.CreateCharacterUseCase;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.mapper.ResponseMapper;
import com.example.anime.global.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/characters")
public class CharacterController {
  private final CharacterService characterService;
  private final ResponseMapper responseMapper;
  private final CreateCharacterUseCase createCharacterUseCase;


  @PostMapping
  public ResponseEntity<ResponseDto<Void>> create(@RequestBody @Valid CharacterRequest characterRequest) {
    createCharacterUseCase.execute(characterRequest);
    ResponseDto<Void> responseDto = responseMapper.toResponseDto("create character", null);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping
  public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findAll(@RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
    CursorPage<CharacterResponse> characterResponses = characterService.findAll(cursorId, size);
    ResponseDto<CursorPage<CharacterResponse>> responseDto = responseMapper.toResponseDto("find characters", characterResponses);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{character-id}")
  public ResponseEntity<ResponseDto<CharacterResponse>> findById(@PathVariable("character-id") Long characterId) {
    CharacterResponse characterResponse = characterService.find(characterId);
    ResponseDto<CharacterResponse> responseDto = responseMapper.toResponseDto("find character", characterResponse);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search/anime")
  public ResponseEntity<ResponseDto<List<Long>>> findIdsByAnimeId(@RequestParam("anime-id") Long animeId) {
    List<Long> characterIds = characterService.findIdsByAnime(animeId);
    ResponseDto<List<Long>> responseDto = responseMapper.toResponseDto("find character ids by anime id", characterIds);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search/death-reason")
  public ResponseEntity<ResponseDto<List<Long>>> findIdsByDeathReason(@RequestParam("death-reason") String deathReason) {
    List<Long> characterIds = characterService.findIdsByDeathReason(deathReason);
    ResponseDto<List<Long>> responseDto = responseMapper.toResponseDto("find character ids by death reason", characterIds);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search/characterIds")
  public ResponseEntity<ResponseDto<List<CharacterResponse>>> findCharacterResponsesByCharacterIds(@RequestParam List<Long> characterIds) {
    List<CharacterResponse> characterResponseList = characterService.findByCharacterIds(characterIds);
    ResponseDto<List<CharacterResponse>> responseDto = responseMapper.toResponseDto("find character ids by death reason", characterResponseList);
    return ResponseEntity.ok(responseDto);
  }


}

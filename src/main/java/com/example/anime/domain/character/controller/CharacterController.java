package com.example.anime.domain.character.controller;

import com.example.anime.domain.character.dto.response.CharacterIdResponse;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.domain.character.service.usecase.CharacterImageUploadUseCase;
import com.example.anime.domain.character.service.usecase.CreateCharacterUseCase;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.ResponseDto;
import com.example.anime.global.util.HttpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animes/characters")
public class CharacterController {
  private final CharacterService characterService;
  private final CreateCharacterUseCase createCharacterUseCase;
  private final CharacterImageUploadUseCase characterImageUploadUseCase;

  @PostMapping
  public ResponseEntity<ResponseDto<CharacterIdResponse>> create(@RequestBody @Valid CharacterRequest characterRequest) {
    CharacterIdResponse characterId = createCharacterUseCase.execute(characterRequest);
    ResponseDto<CharacterIdResponse> responseDto = HttpUtil.success("create character", characterId);
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping("/image/{character-id}")
  public ResponseEntity<ResponseDto<Void>> uploadImage(@PathVariable("character-id") Long characterId, @RequestParam("image") MultipartFile image) {
    characterImageUploadUseCase.upload(characterId, image);
    ResponseDto<Void> responseDto = HttpUtil.success("upload image");
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping
  public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findAll(@RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
    CursorPage<CharacterResponse> characterResponses = characterService.findAll(cursorId, size);
    ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find characters", characterResponses);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/{character-id}")
  public ResponseEntity<ResponseDto<CharacterResponse>> findById(@PathVariable("character-id") Long characterId) {
    CharacterResponse characterResponse = characterService.find(characterId);
    ResponseDto<CharacterResponse> responseDto = HttpUtil.success("find character", characterResponse);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search/anime")
  public ResponseEntity<ResponseDto<List<Long>>> findIdsByAnimeId(@RequestParam("animeId") Long animeId, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
    List<Long> characterIds = characterService.findIdsByAnime(animeId, size, cursorId);
    ResponseDto<List<Long>> responseDto = HttpUtil.success("find character ids by anime id", characterIds);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search/death-reason")
  public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findIdsByDeathReason(@RequestParam("deathReason") String deathReason, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
    CursorPage<CharacterResponse> characterResponses = characterService.findAllByDeathReason(deathReason, cursorId, size);
    ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find character ids by death reason", characterResponses);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search/characterIds")
  public ResponseEntity<ResponseDto<List<CharacterResponse>>> findCharacterResponsesByCharacterIds(@RequestParam List<Long> characterIds) {
    List<CharacterResponse> characterResponseList = characterService.findByCharacterIds(characterIds);
    ResponseDto<List<CharacterResponse>> responseDto = HttpUtil.success("find characters by character ids", characterResponseList);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/search/name")
  public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findCharacterResponsesByCharacterName(@RequestParam("name") String name, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
    CursorPage<CharacterResponse> characterResponses = characterService.findAllByName(name, cursorId, size);
    ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find characters", characterResponses);
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{character-id}")
  public ResponseEntity<ResponseDto<Void>> delete(@PathVariable("character-id") Long characterId) {
    characterService.deleteById(characterId);
    ResponseDto<Void> responseDto = HttpUtil.success("delete character by id");
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping("/{character-id}")
  public ResponseEntity<ResponseDto<Void>> update(@PathVariable("character-id") Long characterId, @RequestBody @Valid CharacterRequest characterUpdateRequest) {
    characterService.update(characterUpdateRequest, characterId);
    ResponseDto<Void> responseDto = HttpUtil.success("update character by id");
    return ResponseEntity.ok(responseDto);
  }

}

package com.example.anime.domain.character.controller;

import com.example.anime.domain.character.dto.response.CharacterIdResponse;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.domain.character.service.usecase.CharacterImageUploadUseCase;
import com.example.anime.domain.character.service.usecase.CreateCharacterUseCase;
import com.example.anime.global.dto.ResponseDto;
import com.example.anime.global.util.HttpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animes/characters")
public class CharacterCommandController {
  private final CharacterService characterService;
  private final CreateCharacterUseCase createCharacterUseCase;
  private final CharacterImageUploadUseCase characterImageUploadUseCase;

  @PostMapping
  public ResponseEntity<ResponseDto<CharacterIdResponse>> create(@RequestBody @Valid CharacterRequest characterRequest) {
    CharacterIdResponse characterId = createCharacterUseCase.execute(characterRequest);
    ResponseDto<CharacterIdResponse> responseDto = HttpUtil.success("create character", characterId);
    return ResponseEntity.ok(responseDto);
  }

  @PatchMapping(value="/image/{character-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseDto<Void>> uploadImage(@PathVariable("character-id") Long characterId, @RequestParam("image") MultipartFile image) {
    characterImageUploadUseCase.upload(characterId, image);
    ResponseDto<Void> responseDto = HttpUtil.success("upload image");
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

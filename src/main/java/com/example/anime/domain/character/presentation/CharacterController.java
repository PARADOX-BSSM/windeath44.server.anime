package com.example.anime.domain.character.presentation;

import com.example.anime.domain.character.facade.CharacterFacade;
import com.example.anime.domain.character.presentation.dto.response.CharacterResponse;
import com.example.anime.domain.character.presentation.dto.request.CharacterRequest;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.mapper.ResponseMapper;
import com.example.anime.global.mapper.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/characters")
public class CharacterController {
  private final CharacterService characterService;
  private final ResponseMapper responseMapper;
  private final CharacterFacade characterFacade;


  @PostMapping
  public ResponseEntity<ResponseDto<Void>> create(@RequestBody @Valid CharacterRequest characterRequest) {
    characterFacade.create(characterRequest);
    ResponseDto<Void> responseDto = responseMapper.toResponseDto("create character", null);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping
  public ResponseEntity<ResponseDto<List<CharacterResponse>>> findAll() {
    List<CharacterResponse> characterResponses = characterService.findAll();
    ResponseDto<List<CharacterResponse>> responseDto = responseMapper.toResponseDto("find characters", characterResponses);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDto);
  }

  @GetMapping("/{character-id}")
  public ResponseEntity<ResponseDto<CharacterResponse>> findById(@PathVariable("character-id") Long characterId) {
    CharacterResponse characterResponse = characterService.find(characterId);
    ResponseDto<CharacterResponse> responseDto = responseMapper.toResponseDto("find characters", characterResponse);
    return ResponseEntity.ok(responseDto);
  }


}

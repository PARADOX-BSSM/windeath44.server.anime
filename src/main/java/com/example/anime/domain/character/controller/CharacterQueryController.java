package com.example.anime.domain.character.controller;

import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.ResponseDto;
import com.example.anime.global.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animes/characters")
public class CharacterQueryController {
    private final CharacterService characterService;

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
    public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findIdsByAnimeId(@RequestParam("animeId") List<Long> animeId, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
        CursorPage<CharacterResponse> characterResponse = characterService.findByAnime(animeId, size, cursorId);
        ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find character by anime id", characterResponse);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search/integrated")
    public ResponseEntity<ResponseDto<CursorPage<CharacterResponse>>> findIdsIntegrated(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "animeId", required = false) List<Long> animeId, @RequestParam(value = "deathReason", required = false) String deathReason, @RequestParam(value = "cursorId", required = false) Long cursorId, @RequestParam int size) {
        CursorPage<CharacterResponse> characterResponses = characterService.findAllIntegrated(name, animeId, deathReason, cursorId, size);
        ResponseDto<CursorPage<CharacterResponse>> responseDto = HttpUtil.success("find character ids integrated", characterResponses);
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
}

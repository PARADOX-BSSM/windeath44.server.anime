package com.example.anime.domain.character.service;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.mapper.CharacterMapper;
import com.example.anime.domain.character.repository.CharacterRepository;
import com.example.anime.domain.character.exception.NotFoundCharacterException;
import com.example.anime.global.dto.CursorPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {
  private final CharacterRepository characterRepository;
  private final CharacterMapper characterMapper;

  public Character findById(Long characterId) {
    Character character = findCharacterById(characterId);
    return character;
  }

  public Character findCharacterById(Long characterId) {
    Character character = characterRepository.findById(characterId)
            .orElseThrow(NotFoundCharacterException::getInstance);
    return character;
  }

  public List<Character> findAllByAnime(Anime anime) {
    List<Character> characterList = characterRepository.findAllByAnime(anime);
     return characterList;
  }

  public CursorPage<CharacterResponse> findAll(Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);

    Slice<Character> characterSlice = cursorId == null
            ? characterRepository.findAllPageable(pageable)
            :  characterRepository.findAllByCursorId(cursorId, pageable);
    List<CharacterResponse> characterList = characterMapper.toCharacterListResponse(characterSlice);
    return new CursorPage<>(characterList, characterSlice.hasNext());
  }

  public CharacterResponse find(Long characterId) {
    Character character = findCharacterById(characterId);
    CharacterResponse characterResponse = characterMapper.toCharacterResponse(character);
    return characterResponse;
  }

  public List<Long> findIdsByAnime(Long animeId, int size, Long cursorId) {
    Pageable pageable = PageRequest.of(0, size);

    List<Long> characterIds = cursorId == null
            ? characterRepository.findIdsByAnimeId(animeId, pageable)
            :  characterRepository.findIdsByAnimeIdAndCursorId(animeId, cursorId, pageable);
    return characterIds;
  }

  public List<Long> findIdsByDeathReason(String deathReason, Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);

    List<Long> characterIds = cursorId == null
            ? characterRepository.findIdsByDeathReason(deathReason, pageable)
            :  characterRepository.findIdsByDeathReasonAndCursorId(deathReason, cursorId, pageable);
    return characterIds;
  }

  public List<CharacterResponse> findByCharacterIds(List<Long> characterIds) {
    List<CharacterResponse> characterList = characterRepository.findAllByIds(characterIds)
            .stream()
            .map(characterMapper::toCharacterResponse)
            .toList();
    return characterList;
  }

  public CursorPage<CharacterResponse> findAllByName(String name, Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);
    Slice<Character> characterSlice = cursorId == null
            ? characterRepository.findAllPageableByName(name, pageable)
            : characterRepository.findAllByCursorIdAndName(name, cursorId, pageable);
    List<CharacterResponse> characterList = characterMapper.toCharacterListResponse(characterSlice);
    return new CursorPage<>(characterList, characterSlice.hasNext());
  }

  @Transactional(readOnly = false)
  public Long create(CharacterRequest characterRequest, Anime anime) {
    Character character = characterMapper.toCharacter(characterRequest, anime);
    Character savedCharacter = characterRepository.save(character);
    return savedCharacter.getCharacterId();
  }

  @Transactional(readOnly = false)
  public void memorializing(Long characterId) {
    Character character = characterRepository.findById(characterId)
            .orElseThrow(NotFoundCharacterException::getInstance);
    character.memorializing();
  }

  @Transactional(readOnly = false)
  public void deleteById(Long characterId) {
    Character character = findCharacterById(characterId);
    characterRepository.delete(character);
  }

  @Transactional(readOnly = false)
  public void update(CharacterRequest characterUpdateRequest, Long characterId) {
    Character character = findCharacterById(characterId);
    character.update(characterUpdateRequest);
  }

  @Transactional(readOnly = false)
  public void updateImage(Long characterId, String imageUrl) {
    Character character = findCharacterById(characterId);
    character.updateImage(imageUrl);
  }
}

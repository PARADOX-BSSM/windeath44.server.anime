package com.example.anime.domain.character.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.dto.response.CharacterResponse;
import com.example.anime.domain.character.domain.mapper.CharacterMapper;
import com.example.anime.domain.character.domain.repository.CharacterRepository;
import com.example.anime.domain.character.presentation.dto.request.CharacterRequest;
import com.example.anime.domain.character.service.exception.NotFoundCharacterException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {
  private final CharacterRepository characterRepository;
  private final CharacterMapper characterMapper;
  private final AnimeService animeService;

  public Character findById(Long characterId) {
    Character character = findCharacterById(characterId);
    return character;
  }

  private Character findCharacterById(Long characterId) {
    Character character = characterRepository.findById(characterId)
            .orElseThrow(NotFoundCharacterException::getInstance);
    return character;
  }

  public List<Character> findAllByAnime(Anime anime) {
    List<Character> characterList = characterRepository.findAllByAnime(anime);
     return characterList;
  }

  public List<CharacterResponse> findAll() {
    List<CharacterResponse> characterList = characterRepository.findAll()
            .stream()
            .map(characterMapper::toCharacterResponse)
            .toList();
    return characterList;
  }

  public CharacterResponse find(Long characterId) {
    Character character = findCharacterById(characterId);
    CharacterResponse characterResponse = characterMapper.toCharacterResponse(character);
    return characterResponse;
  }

  public void create(@Valid CharacterRequest characterRequest) {
    Long animeId = characterRequest.animeId();
    String name = characterRequest.name();
    String content = characterRequest.content();
    String deathReason = characterRequest.death_reason();
    Long lifeTime = characterRequest.lifeTime();

    Anime anime = animeService.getAnime(animeId);
    Character character = characterMapper.toCharacter(anime, name, content, deathReason, lifeTime);
    characterRepository.save(character);
  }
}

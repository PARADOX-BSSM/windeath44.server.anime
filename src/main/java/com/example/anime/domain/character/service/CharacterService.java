package com.example.anime.domain.character.service;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.mapper.CharacterMapper;
import com.example.anime.domain.character.repository.CharacterRepository;
import com.example.anime.domain.character.exception.NotFoundCharacterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {
  private final CharacterRepository characterRepository;
  private final CharacterMapper characterMapper;

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

  public void create(Anime anime, String name, String content, String deathReason, Long lifeTime, String imageUrl) {
    Character character = characterMapper.toCharacter(anime, name, content, deathReason, lifeTime, imageUrl);
    characterRepository.save(character);
  }
}

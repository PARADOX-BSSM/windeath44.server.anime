package com.example.anime.domain.character.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.repository.CharacterRepository;
import com.example.anime.domain.character.service.exception.NotFoundCharacterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {
  private final CharacterRepository characterRepository;

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
}

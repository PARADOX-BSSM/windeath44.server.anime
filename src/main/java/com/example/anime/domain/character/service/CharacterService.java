package com.example.anime.domain.character.service;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.dto.request.CharacterIdsRequest;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.mapper.CharacterMapper;
import com.example.anime.domain.character.repository.CharacterRepository;
import com.example.anime.domain.character.exception.NotFoundCharacterException;
import com.example.anime.global.dto.CursorPage;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public CursorPage<CharacterResponse> findAll(Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);

    Slice<Character> characterSlice = cursorId == null ? characterRepository.findAllPageable(pageable) :  characterRepository.findAllByCursorId(cursorId, pageable);

    List<CharacterResponse> characterList = characterSlice.getContent()
            .stream()
            .map(characterMapper::toCharacterResponse)
            .toList();

    return new CursorPage<>(characterList, characterSlice.hasNext());
  }

  public CharacterResponse find(Long characterId) {
    Character character = findCharacterById(characterId);
    CharacterResponse characterResponse = characterMapper.toCharacterResponse(character);
    return characterResponse;
  }

  @Transactional
  public void create(Anime anime, String name, String content, String deathReason, Long lifeTime, String imageUrl) {
    Character character = characterMapper.toCharacter(anime, name, content, deathReason, lifeTime, imageUrl);
    characterRepository.save(character);
  }
  @Transactional
  public void memorializing(Long characterId) {
    Character character = characterRepository.findById(characterId)
            .orElseThrow(NotFoundCharacterException::getInstance);
    character.memorializing();
  }

  public List<Long> findIdsByAnime(Long animeId) {
    List<Long> characterIds = characterRepository.findIdsByAnimeId(animeId);
    return characterIds;
  }

  public List<Long> findIdsByDeathReason(String deathReason) {
   List<Long> characterIds = characterRepository.findIdsByDeathReason(deathReason);
   return characterIds;
  }

  public CharacterAvroSchema transformSchema(MemorialAvroSchema memorialAvroSchema) {
    Character character = findCharacterById(memorialAvroSchema.getCharacterId());

    CharacterAvroSchema characterAvroSchema = characterMapper.toCharacterAvroSchema(character, memorialAvroSchema);
    return characterAvroSchema;
  }

  public List<CharacterResponse> findByCharacterIds(CharacterIdsRequest characterIdsRequest) {
    List<Long> characterIds = characterIdsRequest.characterIds();
    List<CharacterResponse> characterList = characterRepository.findAllByIds(characterIds)
            .stream()
            .map(characterMapper::toCharacterResponse)
            .toList();
    return characterList;
  }
}

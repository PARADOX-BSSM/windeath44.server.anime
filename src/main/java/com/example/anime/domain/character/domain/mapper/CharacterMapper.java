package com.example.anime.domain.character.domain.mapper;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.character.domain.dto.response.CharacterResponse;
import com.example.anime.domain.character.domain.Character;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

  public Character toCharacter(Anime anime, String name, String content, String deathReason, Long lifeTime) {
    return Character.builder()
//            .anime(anime)
            .name(name)
            .content(content)
            .death_reason(deathReason)
            .lifeTime(lifeTime)
            .build();
  }

  public CharacterResponse toCharacterResponse(Character character) {
    Long characterId = character.getCharacterId();
    String name = character.getName();
    String content = character.getContent();
    Long lifeTime = character.getLifeTime();
    String death_reason = character.getDeath_reason();
    return new CharacterResponse(characterId, name, content, lifeTime, death_reason);
  }
}

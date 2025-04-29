package com.example.anime.domain.character.domain.mapper;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.character.domain.Character;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

  public Character toEntity(Anime anime, String name, String content, String deathReason, Long lifeTime) {
    return Character.builder()
//            .anime(anime)
            .name(name)
            .content(content)
            .death_reason(deathReason)
            .lifeTime(lifeTime)
            .build();
  }
}

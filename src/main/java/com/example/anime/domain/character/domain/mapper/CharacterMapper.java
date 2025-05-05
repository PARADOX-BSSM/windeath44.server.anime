package com.example.anime.domain.character.domain.mapper;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.character.domain.CharacterState;
import com.example.anime.domain.character.domain.dto.response.CharacterResponse;
import com.example.anime.domain.character.domain.Character;
import com.example.grpc.GetCharacterResponse;
import org.springframework.stereotype.Component;

import javax.naming.Name;

@Component
public class CharacterMapper {

  public Character toCharacter(Anime anime, String name, String content, String deathReason, Long lifeTime) {
    return Character.builder()
            .anime(anime)
            .name(name)
            .content(content)
            .death_reason(deathReason)
            .lifeTime(lifeTime)
            .state(CharacterState.NOT_MEMORIALIZING)
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

  public GetCharacterResponse toGetCharacterResponse(Character character) {
    Anime anime = character.getAnime();
    Long animeId = anime.getAnimeId();
    String animeName = anime.getName();

    String name = character.getName();
    String content = character.getContent();
    String state = character.getState().toString();

    GetCharacterResponse response = GetCharacterResponse.newBuilder()
            .setAnimeId(animeId)
            .setAnimeName(animeName)
            .setName(name)
            .setContent(content)
            .setState(state)
            .build();
    return response;

  }
}

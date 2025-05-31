package com.example.anime.domain.character.mapper;

import com.example.anime.domain.anime.entity.Anime;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.entity.Character;
import com.example.grpc.GetCharacterResponse;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

  public Character toCharacter(Anime anime, String name, String content, String deathReason, Long lifeTime) {
    return Character.builder()
            .anime(anime)
            .name(name)
            .content(content)
            .deathReason(deathReason)
            .lifeTime(lifeTime)
            .build();
  }

  public CharacterResponse toCharacterResponse(Character character) {
    Long characterId = character.getCharacterId();
    String name = character.getName();
    String content = character.getContent();
    Long lifeTime = character.getLifeTime();
    String death_reason = character.getDeathReason();
    String imageUrl = character.getImageUrl();
    Long bow_count = character.getBowCount();

    return new CharacterResponse(characterId, name, content, lifeTime, death_reason, imageUrl, bow_count);
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

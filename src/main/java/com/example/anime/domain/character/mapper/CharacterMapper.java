package com.example.anime.domain.character.mapper;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.model.Character;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import com.example.grpc.GetCharacterResponse;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

  public Character toCharacter(CharacterRequest characterRequest, Anime anime) {
    String name = characterRequest.name();
    String deathReason = characterRequest.deathReason();
    Long lifeTime = characterRequest.lifeTime();

    return Character.builder()
            .anime(anime)
            .name(name)
            .deathReason(deathReason)
            .lifeTime(lifeTime)
            .build();
  }

  public CharacterResponse toCharacterResponse(Character character) {
    Long characterId = character.getCharacterId();
    String name = character.getName();
    Long lifeTime = character.getLifeTime();
    String death_reason = character.getDeathReason();
    String imageUrl = character.getImageUrl();
    Long bow_count = character.getBowCount();

    return new CharacterResponse(characterId, name, lifeTime, death_reason, imageUrl, bow_count);
  }

  public GetCharacterResponse toGetCharacterResponse(Character character) {
    Anime anime = character.getAnime();
    Long animeId = anime.getAnimeId();
    String animeName = anime.getName();

    String name = character.getName();
    String state = character.getState().toString();

    GetCharacterResponse response = GetCharacterResponse.newBuilder()
            .setAnimeId(animeId)
            .setAnimeName(animeName)
            .setName(name)
            .setState(state)
            .build();
    return response;

  }

  public CharacterAvroSchema toCharacterAvroSchema(Character character, MemorialAvroSchema memorialAvroSchema) {
    Long characterId = character.getCharacterId();
    String name = character.getName();
    String content = "";
    String deathReason = character.getDeathReason();
    String state = character.getState().toString();
    String applicantId = memorialAvroSchema.getWriterId();

    CharacterAvroSchema characterAvroSchema = CharacterAvroSchema.newBuilder()
            .setCharacterId(characterId)
            .setName(name)
            .setContent(content)
            .setDeathReason(deathReason)
            .setState(state)
            .setApplicantId(applicantId)
            .build();
    return characterAvroSchema;
  }
}

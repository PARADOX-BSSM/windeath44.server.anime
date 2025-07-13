package com.example.anime.domain.character.mapper;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.model.Character;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import com.example.grpc.GetCharacterResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CharacterMapper {

  public Character toCharacter(CharacterRequest characterRequest, Anime anime) {
    String name = characterRequest.name();
    String deathReason = characterRequest.deathReason();
    Long lifeTime = characterRequest.lifeTime();
    Integer age = characterRequest.age();
    LocalDateTime deathOfDay = characterRequest.deathOfDay();

    return Character.builder()
            .anime(anime)
            .name(name)
            .age(age)
            .deathReason(deathReason)
            .lifeTime(lifeTime)
            .deathOfDay(deathOfDay)
            .build();
  }

  public CharacterResponse toCharacterResponse(Character character) {
    Long characterId = character.getCharacterId();
    String name = character.getName();
    Long lifeTime = character.getLifeTime();
    String deathReason = character.getDeathReason();
    String imageUrl = character.getImageUrl();
    Long bow_count = character.getBowCount();
    LocalDateTime deathOfDay = character.getDeathOfDay();

    return CharacterResponse.builder()
            .characterId(characterId)
            .name(name)
            .lifeTime(lifeTime)
            .deathReason(deathReason)
            .imageUrl(imageUrl)
            .bow_count(bow_count)
            .death_of_day(deathOfDay)
            .build();
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

  public List<CharacterResponse> toCharacterListResponse(Slice<Character> characterSlice) {
    return characterSlice.getContent()
            .stream()
            .map(this::toCharacterResponse)
            .toList();
  }
}

package com.example.anime.domain.character.mapper;
import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.dto.response.CharacterIdResponse;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.model.type.CauseOfDeath;
import com.example.anime.domain.character.model.type.CharacterState;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import com.example.grpc.GetCharacterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class CharacterMapper {

  public Character toCharacter(CharacterRequest characterRequest, Anime anime) {
    String name = characterRequest.name();
    CauseOfDeath deathReason = CauseOfDeath.valueOfDeathReason(characterRequest.deathReason());
    Long lifeTime = characterRequest.lifeTime();
    Integer age = characterRequest.age();
    LocalDate deathOfDay = characterRequest.deathOfDay();

    String saying =  characterRequest.saying();

    return Character.builder()
            .anime(anime)
            .name(name)
            .age(age)
            .saying(saying)
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
    LocalDate deathOfDay = character.getDeathOfDay();
    CharacterState state = character.getState();
    Long animeId = character.getAnimeId();
    Integer age = character.getAge();
    String saying = character.getSaying();

    return CharacterResponse.builder()
            .characterId(characterId)
            .animeId(animeId)
            .name(name)
            .lifeTime(lifeTime)
            .deathReason(deathReason)
            .imageUrl(imageUrl)
            .bowCount(bow_count)
            .deathOfDay(deathOfDay)
            .state(state.toString())
            .age(age)
            .saying(saying)
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

  public CharacterIdResponse toCharacterIdResponse(Character savedCharacter) {
    return new CharacterIdResponse(savedCharacter.getCharacterId());
  }
}

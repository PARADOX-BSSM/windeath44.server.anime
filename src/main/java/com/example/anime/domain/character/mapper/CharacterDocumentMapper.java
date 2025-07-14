package com.example.anime.domain.character.mapper;

import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.model.CharacterDocument;
import com.example.anime.domain.character.model.CharacterState;
import com.example.anime.global.dto.DocumentSlice;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CharacterDocumentMapper {

    public List<CharacterResponse> toCharacterList(DocumentSlice<CharacterDocument> characterDocumentSlice) {
        return characterDocumentSlice.getContent()
                .stream()
                .map(this::toCharacter)
                .toList();
    }

    private CharacterResponse toCharacter(CharacterDocument characterDocument) {
        Long characterId = characterDocument.getCharacterId();
        Long animeId =  characterDocument.getAnimeId();
        String name = characterDocument.getName();
        Integer age = characterDocument.getAge();
        Long lifeTime =  characterDocument.getLifeTime();
        String deathReason =  characterDocument.getDeathReason();
        String imageUrl =  characterDocument.getImageUrl();
        CharacterState state =  characterDocument.getState();
        Long bowCount = characterDocument.getBowCount();
        LocalDate deathOfDay = characterDocument.getDeathOfDay();

        return CharacterResponse.builder()
                .characterId(characterId)
                .animeId(animeId)
                .name(name)
                .age(age)
                .lifeTime(lifeTime)
                .deathReason(deathReason)
                .deathOfDay(deathOfDay)
                .bowCount(bowCount)
                .imageUrl(imageUrl)
                .state(state.toString())
                .build();
    }
}

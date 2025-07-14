package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.dto.response.CharacterIdResponse;
import com.example.anime.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCharacterUseCase {
  private final AnimeService animeService;
  private final CharacterService characterService;

  public CharacterIdResponse execute(CharacterRequest characterRequest) {
    Long animeId = characterRequest.animeId();

    Anime anime = animeService.getAnime(animeId);
    CharacterIdResponse characterId = characterService.create(characterRequest, anime);
    return characterId;
  }

}

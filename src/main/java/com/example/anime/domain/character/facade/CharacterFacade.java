package com.example.anime.domain.character.facade;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.presentation.dto.request.CharacterRequest;
import com.example.anime.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterFacade {
  private final AnimeService animeService;
  private final CharacterService characterService;


  public void create(CharacterRequest characterRequest) {
    Long animeId = characterRequest.animeId();
    String name = characterRequest.name();
    String content = characterRequest.content();
    String deathReason = characterRequest.death_reason();
    Long lifeTime = characterRequest.lifeTime();

    Anime anime = animeService.getAnime(animeId);
    characterService.create(anime, name, content, deathReason, lifeTime);
  }

}

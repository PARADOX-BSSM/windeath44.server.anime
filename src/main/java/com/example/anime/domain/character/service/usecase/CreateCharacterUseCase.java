package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.exception.UploadFileFailException;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CreateCharacterUseCase {
  private final AnimeService animeService;
  private final CharacterService characterService;
  private final FileStorage fileStorage;

  public void execute(CharacterRequest characterRequest) {
    Long animeId = characterRequest.animeId();
    String name = characterRequest.name();
    String content = characterRequest.content();
    String deathReason = characterRequest.deathReason();
    Long lifeTime = characterRequest.lifeTime();
    MultipartFile image = characterRequest.image();
    String imageUrl = "";

    try {
      imageUrl = fileStorage.upload(image);
    } catch(IOException e) {
      throw UploadFileFailException.getInstance();
    }

    Anime anime = animeService.getAnime(animeId);
    characterService.create(anime, name, content, deathReason, lifeTime, imageUrl);
  }

}

package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.dto.request.CharacterUpdateRequest;
import com.example.anime.domain.character.exception.UploadFileFailException;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.storage.FileStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UpdateCharacterUseCase {
  private final CharacterService characterService;
  private final FileStorage fileStorage;

  public void execute(CharacterUpdateRequest characterUpdateRequest, Long charterId) {
    String name = characterUpdateRequest.name();
    String content = characterUpdateRequest.content();
    String deathReason = characterUpdateRequest.deathReason();
    Long lifeTime = characterUpdateRequest.lifeTime();
    MultipartFile image = characterUpdateRequest.image();
    String imageUrl = "";

    try {
      imageUrl = fileStorage.upload(image);
    } catch(IOException e) {
      throw UploadFileFailException.getInstance();
    }
    characterService.update(charterId, name, content, deathReason, lifeTime, imageUrl);
  }
}

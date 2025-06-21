package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.character.exception.UploadFileFailException;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CharacterImageUploadUseCase {
  private final FileStorage fileStorage;
  private final CharacterService characterService;

  public void upload(Long characterId, MultipartFile image) {
    String imageUrl = "";
    try {
      imageUrl = fileStorage.upload(characterId.toString(), image);
    } catch(IOException e) {
      throw UploadFileFailException.getInstance();
    }
    characterService.updateImage(characterId, imageUrl);
  }
}

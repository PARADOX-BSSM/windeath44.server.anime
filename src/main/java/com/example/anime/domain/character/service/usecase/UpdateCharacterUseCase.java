package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.character.exception.UploadFileFailException;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.storage.FileStorage;
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
    MultipartFile image = characterUpdateRequest.image();

    String imageUrl = "";

    try {
      imageUrl = fileStorage.upload(charterId.toString(), image);
    } catch(IOException e) {
      throw UploadFileFailException.getInstance();
    }

    characterService.update(characterUpdateRequest, charterId, imageUrl);
  }
}

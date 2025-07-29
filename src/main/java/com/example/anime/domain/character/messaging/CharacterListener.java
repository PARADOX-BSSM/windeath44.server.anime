package com.example.anime.domain.character.messaging;

import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.domain.character.service.usecase.MemorializingCharacterUseCase;
import com.example.avro.MemorialAvroSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CharacterListener {
  private final MemorializingCharacterUseCase memorializingCharacterUseCase;

  @KafkaListener(topics = "character-memorializing-request", groupId = "memorial")
  public void memorializingCharacter(MemorialAvroSchema message) {
    memorializingCharacterUseCase.memorializing(message);
  }

}

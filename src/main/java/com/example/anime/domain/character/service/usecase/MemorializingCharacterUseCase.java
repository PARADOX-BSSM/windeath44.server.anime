package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.character.exception.NotFoundCharacterException;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.kafka.KafkaProducer;
import com.example.avro.MemorialAvroSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemorializingCharacterUseCase {
  private final CharacterService characterService;
  private final KafkaProducer kafkaProducer;

  public void memorializing(MemorialAvroSchema memorialAvroSchema) {
    try {
      Long characterId = memorialAvroSchema.getCharacterId();
      characterService.memorializing(characterId);
    } catch (Exception e) {
      log.error("memorializing character fail. characterId: {}", memorialAvroSchema.getCharacterId());
      log.error(e.getMessage());
      kafkaProducer.send("character-memorializing-fail", memorialAvroSchema);
    }

  }
}

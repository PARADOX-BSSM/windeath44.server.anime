package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.character.exception.NotFoundCharacterException;
import com.example.anime.domain.character.mapper.CharacterMapper;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.kafka.KafkaProducer;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemorializingCharacterUseCase {
  private final CharacterService characterService;
  private final KafkaProducer kafkaProducer;
  private final CharacterMapper characterMapper;

  @Transactional
  public void memorializing(MemorialAvroSchema memorialAvroSchema) {
    Character character = characterService.findCharacterById(memorialAvroSchema.getCharacterId());
    CharacterAvroSchema characterAvroSchema = characterMapper.toCharacterAvroSchema(character, memorialAvroSchema);

    try {
      Long characterId = characterAvroSchema.getCharacterId();
      characterService.memorializing(characterId);
      kafkaProducer.send("character-memorialized-response", characterAvroSchema);
    } catch (Exception e) {
      log.error("memorializing character fail. characterId: {}", characterAvroSchema.getCharacterId());
      log.error(e.getMessage());
      kafkaProducer.send("character-memorializing-fail-response", characterAvroSchema);
    }

  }
}

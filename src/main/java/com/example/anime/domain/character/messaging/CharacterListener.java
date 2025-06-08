package com.example.anime.domain.character.messaging;

import com.example.anime.domain.character.service.CharacterService;
import com.example.avro.MemorialAvroSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CharacterListener {
  private final CharacterService characterService;;

  @KafkaListener(topics = "character-memorializing", groupId = "memorial")
  public void memorializingCharacter(MemorialAvroSchema message) {
    Long characterId = message.getCharacterId();
    characterService.memorializing(characterId);
  }

}

package com.example.anime.domain.character.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundCharacter extends RuntimeException  {
  public NotFoundCharacter(String s, Long characterId) {
    super(s);
    log.error(characterId.toString());
  }
}

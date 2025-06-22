package com.example.anime.domain.character.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class NotFoundCharacterException extends GlobalException {

  public NotFoundCharacterException() {
    super(ErrorCode.CHARACTER_NOT_FOUND);
  }

  private static class Holder {
    private final static NotFoundCharacterException INSTANCE = new NotFoundCharacterException();
  }

  public static NotFoundCharacterException getInstance() {
    return Holder.INSTANCE;
  }
}
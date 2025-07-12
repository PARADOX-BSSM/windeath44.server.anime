package com.example.anime.domain.anime.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class AlreadyCachedAnimeException extends GlobalException {

  public AlreadyCachedAnimeException() {
    super(ErrorCode.ANIME_ALREADY_CACHED);
  }

  class Holder {
    private static final AlreadyCachedAnimeException INSTANCE = new AlreadyCachedAnimeException();
  }

  public static AlreadyCachedAnimeException getInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public String getMessage() {
    return this.getErrorCode().getMessage();
  }

}

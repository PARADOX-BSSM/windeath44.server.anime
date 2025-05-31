package com.example.anime.domain.anime.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class NotFoundAnimeException extends GlobalException {
  public NotFoundAnimeException() {
    super(ErrorCode.ANIME_NOT_FOUND);
  }

  private static class Holder {
    private static final NotFoundAnimeException INSTANCE = new NotFoundAnimeException();
  }

  public static NotFoundAnimeException getInstance() {
    return Holder.INSTANCE;
  }

}
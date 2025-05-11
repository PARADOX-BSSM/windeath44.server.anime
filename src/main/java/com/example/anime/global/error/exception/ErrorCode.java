package com.example.anime.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ANIME_NOT_FOUND(404, "Anime not found"),
  CHARACTER_NOT_FOUND(404, "Character not found")
  ;
  private int status;
  private String message;
}

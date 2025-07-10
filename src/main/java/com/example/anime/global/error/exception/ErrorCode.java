package com.example.anime.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ANIME_NOT_FOUND(404, "Anime not found"),
  ANIME_ALREADY_CACHED(500, "Anime already cached"),
  CHARACTER_NOT_FOUND(404, "Character not found"),
  FILE_UPLOAD_FAILED(500, "File upload failed")
  ;
  private int status;
  private String message;
}

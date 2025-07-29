package com.example.anime.global.error;
import com.example.anime.global.error.exception.ErrorCode;

public record ErrorResponse (
        int status,
        String message
) {
  public ErrorResponse(ErrorCode errorCode) {
    this(
            errorCode.getStatus(),
            errorCode.getMessage()
    );
  }
}

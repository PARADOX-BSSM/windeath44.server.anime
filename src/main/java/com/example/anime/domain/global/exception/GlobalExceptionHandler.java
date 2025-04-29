package com.example.anime.domain.global.exception;

import com.example.anime.domain.anime.service.exception.NotFoundAnimation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  
  @ExceptionHandler(NotFoundAnimation.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void notFoundAnimationHanlder(NotFoundAnimation e) {
    errorLogging(e);
  }

  private void errorLogging(RuntimeException e) {
    log.error(e.getMessage());
    log.error(e.getStackTrace().toString());
  }
}

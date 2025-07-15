package com.example.anime.global.error;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ErrorResponse> customGlobalExceptionHandler(GlobalException e) {
    ErrorCode errorCode = e.getErrorCode();
    int status = errorCode.getStatus();
    errorLogging(errorCode, e);
    return new ResponseEntity<>(
            new ErrorResponse(errorCode),
            HttpStatus.valueOf(status)
    );
  }

  private void errorLogging(ErrorCode errorCode, GlobalException e) {
    log.error("error message : {} || error status : {}", errorCode.getMessage(), errorCode.getStatus());
    log.error(e.getStackTrace().toString());
  }

}

package com.example.anime.domain.character.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class UploadFileFailException extends GlobalException {
  public UploadFileFailException() {
    super(ErrorCode.FILE_UPLOAD_FAILED);
  }
  private static class Holder {
    private static final UploadFileFailException INSTANCE = new UploadFileFailException();
  }
  public static UploadFileFailException getInstance() {
    return Holder.INSTANCE;
  }
}

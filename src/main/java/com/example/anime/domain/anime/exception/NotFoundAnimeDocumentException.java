package com.example.anime.domain.anime.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class NotFoundAnimeDocumentException extends GlobalException {
    public NotFoundAnimeDocumentException() {
        super(ErrorCode.ANIME_DOCUMENT_NOT_FOUND);
    }
    static class Holder {
        private final static NotFoundAnimeDocumentException INSTANCE = new NotFoundAnimeDocumentException();
    }

    public static NotFoundAnimeDocumentException getInstance() {
        return Holder.INSTANCE;
    }
}

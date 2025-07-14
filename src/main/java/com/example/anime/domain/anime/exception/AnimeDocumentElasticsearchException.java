package com.example.anime.domain.anime.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class AnimeDocumentElasticsearchException extends GlobalException {
    public AnimeDocumentElasticsearchException() {
        super(ErrorCode.ANIME_DOCUMENT_ELASTICSEARCH_ERROR);
    }
    static class Holder {
        private final static AnimeDocumentElasticsearchException INSTANCE = new AnimeDocumentElasticsearchException();
    }

    public static AnimeDocumentElasticsearchException getInstance() {
        return Holder.INSTANCE;
    }
}

package com.example.anime.domain.character.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class CharacterDocumentElasticsearchException extends GlobalException  {
    public CharacterDocumentElasticsearchException() {
        super(ErrorCode.CHARACTER_DOCUMENT_ELASTICSEARCH_ERROR);
    }

    static class Holder {
        private final static CharacterDocumentElasticsearchException INSTANCE = new CharacterDocumentElasticsearchException();
    }

    public static CharacterDocumentElasticsearchException getInstance() {
        return Holder.INSTANCE;
    }
}

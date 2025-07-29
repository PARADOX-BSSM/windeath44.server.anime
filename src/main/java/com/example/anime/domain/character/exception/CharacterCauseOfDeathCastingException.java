package com.example.anime.domain.character.exception;

import com.example.anime.global.error.exception.ErrorCode;
import com.example.anime.global.error.exception.GlobalException;

public class CharacterCauseOfDeathCastingException extends GlobalException {
    public CharacterCauseOfDeathCastingException() {
        super(ErrorCode.CHARACTER_CAUSE_OF_DEATH_CASTING_FAILED);
    }
    static class Holder {
        private final static CharacterCauseOfDeathCastingException INSTANCE = new CharacterCauseOfDeathCastingException();
    }

    public  static CharacterCauseOfDeathCastingException getInstance() {
        return Holder.INSTANCE;
    }
}

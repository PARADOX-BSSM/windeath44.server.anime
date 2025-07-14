package com.example.anime.domain.character.repository;

import com.example.anime.domain.character.model.CharacterDocument;
import com.example.anime.global.dto.DocumentSlice;

public interface CharacterDocumentCursorRepository {
    DocumentSlice<CharacterDocument> findCharactersByName(int size, String characterName);
    DocumentSlice<CharacterDocument> findCharactersByCursorIdAndName(Long cursorId, int size, String characterName);
}

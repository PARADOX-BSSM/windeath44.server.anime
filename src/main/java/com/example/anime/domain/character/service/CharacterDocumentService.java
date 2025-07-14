package com.example.anime.domain.character.service;

import co.elastic.clients.util.TriFunction;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.mapper.CharacterDocumentMapper;
import com.example.anime.domain.character.model.CharacterDocument;
import com.example.anime.domain.character.repository.elasticsearch.CharacterDocumentRepository;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.DocumentSlice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class CharacterDocumentService {
    private final CharacterDocumentRepository characterDocumentRepository;
    private final CharacterDocumentMapper characterDocumentMapper;

    public CursorPage<CharacterResponse> findAllByName(String name, Long cursorId, int size) {
        return findCharacter(
                cursorId,
                size,
                name,
                characterDocumentRepository::findCharactersByName,
                characterDocumentRepository::findCharactersByCursorIdAndName
        );
    }

    public CursorPage<CharacterResponse> findAllByDeathReason(String deathReason, Long cursorId, int size) {
        return findCharacter(
                cursorId,
                size,
                deathReason,
                characterDocumentRepository::findCharactersByDeathReason,
                characterDocumentRepository::findCharactersByCursorIdAndDeathReason
        );
    }

    private CursorPage<CharacterResponse> findCharacter(
            Long cursorId,
            int size,
            String feild,
            BiFunction<Integer, String, DocumentSlice<CharacterDocument>> findCharacter,
            TriFunction<Long, Integer, String, DocumentSlice<CharacterDocument>> findCharacterByCursorId
    ) {
        DocumentSlice<CharacterDocument> characterDocumentSlice = cursorId == null
                ? findCharacter.apply(size, feild)
                : findCharacterByCursorId.apply(cursorId, size, feild);
        List<CharacterResponse> characterList = characterDocumentMapper.toCharacterList(characterDocumentSlice);
        return new CursorPage<>(characterList, characterDocumentSlice.hasNext());
    }
}

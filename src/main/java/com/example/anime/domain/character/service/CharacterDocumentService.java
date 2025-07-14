package com.example.anime.domain.character.service;

import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.mapper.CharacterDocumentMapper;
import com.example.anime.domain.character.model.CharacterDocument;
import com.example.anime.domain.character.repository.CharacterDocumentRepository;
import com.example.anime.global.dto.CursorPage;
import com.example.anime.global.dto.DocumentSlice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterDocumentService {
    private final CharacterDocumentRepository characterDocumentRepository;
    private final CharacterDocumentMapper characterDocumentMapper;

    public CursorPage<CharacterResponse> findAllByName(String name, Long cursorId, int size) {
        DocumentSlice<CharacterDocument> characterDocumentSlice = cursorId == null
                ? characterDocumentRepository.findCharactersByName(size, name)
                : characterDocumentRepository.findCharactersByCursorIdAndName(cursorId, size, name);
        List<CharacterResponse> characterList = characterDocumentMapper.toCharacterList(characterDocumentSlice);
        return new CursorPage<>(characterList, characterDocumentSlice.hasNext());
    }

}

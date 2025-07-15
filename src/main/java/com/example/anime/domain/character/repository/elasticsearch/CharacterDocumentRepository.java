package com.example.anime.domain.character.repository.elasticsearch;

import com.example.anime.domain.character.model.CharacterDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CharacterDocumentRepository extends ElasticsearchRepository<CharacterDocument, Long>, CharacterDocumentCursorRepository {
}

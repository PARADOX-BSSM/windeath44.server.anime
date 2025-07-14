package com.example.anime.domain.anime.repository.elasticsearch;

import com.example.anime.domain.anime.model.AnimeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AnimeDocumentRepository extends ElasticsearchRepository<AnimeDocument, String>, AnimeDocumentCursorRepository {
}

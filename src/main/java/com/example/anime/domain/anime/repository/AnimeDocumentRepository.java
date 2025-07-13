package com.example.anime.domain.anime.repository;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.model.AnimeDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeDocumentRepository extends ElasticsearchRepository<AnimeDocument, Long>, AnimeDocumentCursorRepository {
}

package com.example.anime.domain.anime.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.anime.domain.anime.exception.NotFoundAnimeDocumentException;
import com.example.anime.domain.anime.model.AnimeDocument;
import com.example.anime.domain.anime.repository.AnimeDocumentCursorRepository;
import com.example.anime.global.dto.DocumentSlice;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnimeDocumentCursorRepositoryImpl implements AnimeDocumentCursorRepository {
    private final ElasticsearchClient elasticsearchClient;

    public AnimeDocumentCursorRepositoryImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public DocumentSlice<AnimeDocument> findAnimesByName(int size, String animeName) {
        return findAnimesByCursorIdAndName(null, size, animeName);
    }

    @Override
    public DocumentSlice<AnimeDocument> findAnimesByCursorIdAndName(Long cursorId, int size, String animeName) {
        try {
            Long[] lastSortValues = cursorId == null ? null : new Long[] {cursorId};
            return searchAfter(lastSortValues, size, animeName);
        } catch (IOException e) {
            e.printStackTrace();
            throw NotFoundAnimeDocumentException.getInstance();
        }
    }

    public DocumentSlice<AnimeDocument> searchAfter(Long[] lastSortValues, int size, String animeName) throws IOException {
        int fetchSize = size + 1;

        SearchRequest.Builder builder = searchRequest("anime", fetchSize, "id", "name", animeName);
        searchAfter(lastSortValues, builder);
        SearchResponse<AnimeDocument> response = elasticsearchClient.search(builder.build(), AnimeDocument.class);

        List<Hit<AnimeDocument>> hits = response.hits().hits();
        boolean hasNext = hits.size() == fetchSize;

        List<AnimeDocument> results = hits.stream()
                .limit(size)
                .map(Hit::source)
                .collect(Collectors.toList());

        return new DocumentSlice<>(results, hasNext);
    }

    private void searchAfter(Long[] lastSortValues, SearchRequest.Builder builder) {
        if (lastSortValues != null && lastSortValues.length > 0) {
            List<FieldValue> fieldValues = Arrays.stream(lastSortValues)
                    .map(FieldValue::of)
                    .toList();
            builder.searchAfter(fieldValues);
        }
    }

    private SearchRequest.Builder searchRequest(String index, int size, String orderValue, String field, String query) {
        return new SearchRequest.Builder()
                .index(index)
                .size(size)
                .sort(s -> s.field(f -> f.field(orderValue).order(SortOrder.Desc)))
                .query(q -> q
                        .match(m -> m
                                .field(field)
                                .query(query)
                        )
                );
    }

}
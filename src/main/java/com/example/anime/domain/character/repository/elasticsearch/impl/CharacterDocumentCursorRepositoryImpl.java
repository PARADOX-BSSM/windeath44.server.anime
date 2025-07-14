package com.example.anime.domain.character.repository.elasticsearch.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.anime.domain.anime.exception.NotFoundAnimeDocumentException;
import com.example.anime.domain.character.model.CharacterDocument;
import com.example.anime.domain.character.repository.elasticsearch.CharacterDocumentCursorRepository;
import com.example.anime.global.dto.DocumentSlice;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CharacterDocumentCursorRepositoryImpl implements CharacterDocumentCursorRepository {
    private final ElasticsearchClient elasticsearchClient;

    public CharacterDocumentCursorRepositoryImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public DocumentSlice<CharacterDocument> findCharactersByName(int size, String characterName) {
        return findCharactersByCursorIdAndName(null, size, characterName);
    }

    @Override
    public DocumentSlice<CharacterDocument> findCharactersByCursorIdAndName(Long cursorId, int size, String characterName) {
        return getCharacterDocuments(cursorId, size, characterName, "name");
    }

    @Override
    public DocumentSlice<CharacterDocument> findCharactersByDeathReason(int size, String deathReason) {
        return findCharactersByCursorIdAndDeathReason(null, size, deathReason);
    }

    @Override
    public DocumentSlice<CharacterDocument> findCharactersByCursorIdAndDeathReason(Long cursorId, int size, String deathReason) {
        return getCharacterDocuments(cursorId, size, deathReason, "death_reason");
    }

    private DocumentSlice<CharacterDocument> getCharacterDocuments(Long cursorId, int size, String characterName, String field) {
        try {
            Long[] lastSortValues = cursorId == null ? null : new Long[] {cursorId};
            return searchAfter(lastSortValues, size, characterName, field);
        } catch (IOException e) {
            e.printStackTrace();
            throw NotFoundAnimeDocumentException.getInstance();
        }
    }


    public DocumentSlice<CharacterDocument> searchAfter(Long[] lastSortValues, int size, String characterName, String field) throws IOException {
        int fetchSize = size + 1;

        SearchRequest.Builder builder = searchRequest("character", fetchSize, "character_id", field, characterName);
        searchAfter(lastSortValues, builder);
        SearchResponse<CharacterDocument> response = elasticsearchClient.search(builder.build(), CharacterDocument.class);

        List<Hit<CharacterDocument>> hits = response.hits().hits();
        boolean hasNext = hits.size() == fetchSize;

        List<CharacterDocument> results = hits.stream()
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

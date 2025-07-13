package com.example.anime.domain.anime.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.ElementCollection;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Getter
@Document(indexName = "anime")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeDocument {
    @Id
    private Long id;
    private String name;
    private List<String> genres;
    private String imageUrl;
}

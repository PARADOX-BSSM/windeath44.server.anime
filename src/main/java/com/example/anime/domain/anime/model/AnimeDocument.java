package com.example.anime.domain.anime.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ElementCollection;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Document(indexName = "anime")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeDocument {
    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String name;
    private List<String> genres;
    @Field(name = "image_url", type = FieldType.Keyword)
    @JsonProperty("image_url")
    private String imageUrl;
}

package com.example.anime.domain.character.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(indexName="character")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class CharacterDocument {
    @Id
    private Long characterId;
    private Long animeId;
    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String name;
    private Integer age;
    private Long lifeTime;
    private String saying;
    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String deathReason;
    private String imageUrl;
    private CharacterState state;
    private Long bowCount;
    private LocalDate deathOfDay;
}

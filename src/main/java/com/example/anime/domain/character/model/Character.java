package com.example.anime.domain.character.model;


import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name="`character`")
public class Character {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long characterId;

  @ManyToOne
  @JoinColumn(name="anime_id")
  private Anime anime;
  private String name;
  private Integer age;
  private Long lifeTime;
  private String deathReason;
  private String imageUrl;
  @Enumerated(EnumType.STRING)
  private CharacterState state;
  private Long bowCount;
  private LocalDateTime death_of_day;

  @PrePersist
  public void init() {
    this.bowCount = 0L;
    this.state = CharacterState.NOT_MEMORIALIZING;
  }

  public void memorializing() {
    this.state = CharacterState.MEMORIALIZING;
  }

  public void update(CharacterRequest characterUpdateRequest, String imageUrl) {
    String name = characterUpdateRequest.name();
    String deathReason = characterUpdateRequest.deathReason();
    Long lifeTime = characterUpdateRequest.lifeTime();
    Integer age = characterUpdateRequest.age();
    LocalDateTime death_of_day = characterUpdateRequest.death_of_day();

    this.name = name;
    this.age = age;
    this.deathReason = deathReason;
    this.lifeTime = lifeTime;
    this.imageUrl = imageUrl;
    this.death_of_day = death_of_day;
  }

  public void updateImage(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}

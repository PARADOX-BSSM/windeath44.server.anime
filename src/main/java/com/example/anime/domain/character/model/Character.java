package com.example.anime.domain.character.model;


import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.model.type.CauseOfDeath;
import com.example.anime.domain.character.model.type.CharacterState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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
  @Enumerated(EnumType.STRING)
  private CauseOfDeath deathReason;
  private String imageUrl;
  private String saying;
  @Enumerated(EnumType.STRING)
  private CharacterState state;
  private Long bowCount;
  private LocalDate deathOfDay;

  @PrePersist
  public void init() {
    this.bowCount = 0L;
    this.state = CharacterState.NOT_MEMORIALIZING;
  }

  public String getDeathReason() {
    return deathReason.getDeathReason();
  }

  public void memorializing() {
    this.state = CharacterState.MEMORIALIZING;
  }

  public void update(CharacterRequest characterUpdateRequest) {
    String name = characterUpdateRequest.name();
    CauseOfDeath deathReason = CauseOfDeath.valueOf(characterUpdateRequest.deathReason());
    Long lifeTime = characterUpdateRequest.lifeTime();
    Integer age = characterUpdateRequest.age();
    LocalDate deathOfDay = characterUpdateRequest.deathOfDay();

    this.name = name;
    this.age = age;
    this.deathReason = deathReason;
    this.lifeTime = lifeTime;
    this.deathOfDay = deathOfDay;
  }

  public void updateImage(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Long getAnimeId() {
    return this.anime.getAnimeId();
  }
}

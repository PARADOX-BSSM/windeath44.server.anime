package com.example.anime.domain.character.model;


import com.example.anime.domain.anime.model.Anime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private String content;
  private Long lifeTime;
  private String deathReason;
  private String imageUrl;
  @Enumerated(EnumType.STRING)
  private CharacterState state;
  private Long bowCount;

  @PrePersist
  public void init() {
    this.bowCount = 0L;
    this.state = CharacterState.NOT_MEMORIALIZING;
  }

  public void memorializing() {
    this.state = CharacterState.MEMORIALIZING;
  }

  public void update(String name, String content, String deathReason, Long lifeTime, String imageUrl) {
    this.name = name;
    this.content = content;
    this.deathReason = deathReason;
    this.lifeTime = lifeTime;
    this.imageUrl = imageUrl;
  }
}

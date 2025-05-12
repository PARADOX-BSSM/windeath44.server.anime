package com.example.anime.domain.character.domain;


import com.example.anime.domain.anime.domain.Anime;
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
  private String death_reason;
  private String image_url;
  @Enumerated(EnumType.STRING)
  private CharacterState state;

  @PrePersist
  public void init() {
    this.state = CharacterState.NOT_MEMORIALIZING;
  }
}

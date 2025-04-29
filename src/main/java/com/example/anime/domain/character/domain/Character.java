package com.example.anime.domain.character.domain;


import com.example.anime.domain.anime.domain.Anime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Character {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long characterId;

  @ManyToOne
  @JoinColumn(name="anime_id")
  private Anime anime;
  private String content;
  private Long lifeTime;
  private String death_reason;
  @Enumerated(EnumType.STRING)
  private CharacterState state;
}

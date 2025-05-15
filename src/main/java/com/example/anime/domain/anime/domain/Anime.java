package com.example.anime.domain.anime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import com.example.anime.domain.character.domain.Character;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Anime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long animeId;
  private String name;
  private String description;
  @Embedded
  private AnimeAirDates airDates;
  @ElementCollection
  private List<String> tags;
  private Long bowCount;
  private String imageUrl;

  @OneToMany(mappedBy = "anime", orphanRemoval = true)
  private List<Character> characterList;

  @PrePersist
  public void init() {
    this.bowCount = 0L;
  }

  public void renewalBowCount() {
    Long bowCount = characterList.stream()
            .mapToLong(c -> c.getBowCount())
            .sum();
    this.bowCount = bowCount;
  }

  public void update(String name, String description, LocalDate startYear, LocalDate endYear, List<String> tags) {
    this.name = name;
    this.description = description;
    this.airDates.update(startYear, endYear);
    this.tags = tags;
  }
}
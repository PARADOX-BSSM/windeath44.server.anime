package com.example.anime.domain.anime.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Getter
public class AnimeAirDates {
  private LocalDate startYear;
  private LocalDate endYear;

  // 방영일 구하기
  public Long getAirDay() {
    if(this.endYear == null) {
      return null;
    }
    Long dday = ChronoUnit.DAYS.between(this.startYear, this.endYear);
    return dday;
  }
  public void update(LocalDate startYear, LocalDate endYear) {
    this.startYear = startYear;
    this.endYear = endYear;
  }
}

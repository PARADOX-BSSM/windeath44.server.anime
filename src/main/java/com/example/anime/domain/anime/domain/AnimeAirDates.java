package com.example.anime.domain.anime.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Getter
public class AnimeAirDates {
  private LocalDate start_year;
  private LocalDate end_year;

  // 방영일 구하기
  public Long getAirDay() {
    if(this.end_year == null) {
      return null;
    }
    Long dday = ChronoUnit.DAYS.between(this.start_year, this.end_year);
    return dday;
  }
  public void update(LocalDate startYear, LocalDate endYear) {
    this.start_year = startYear;
    this.end_year = endYear;
  }
}

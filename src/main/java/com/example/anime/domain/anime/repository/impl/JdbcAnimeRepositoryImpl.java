package com.example.anime.domain.anime.repository.impl;
import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.repository.JdbcAnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcAnimeRepositoryImpl implements JdbcAnimeRepository {
  private final JdbcTemplate jdbcTemplate;

  public void bulkInsert(List<Anime> animeList) {
    String sql = "insert into anime (anime_id, name, image_url) values (?, ?, ?)";
    jdbcTemplate.batchUpdate(sql,
            new BatchPreparedStatementSetter() {
              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                Anime anime = animeList.get(i);
                ps.setLong(1, anime.getAnimeId());
                ps.setString(2, anime.getName());
                ps.setString(3, anime.getImageUrl());
              }

              @Override
              public int getBatchSize() {
                return animeList.size();
              }
            });

    String sql2 = "INSERT INTO anime_genres (anime_anime_id, genres) VALUES (?, ?)";
    List<AnimeGenreParam> genreParams = new ArrayList<>();

    for (Anime anime : animeList) {
      for (String genre : anime.getGenres()) {
        genreParams.add(new AnimeGenreParam(anime.getAnimeId(), genre));
      }
    }

    jdbcTemplate.batchUpdate(sql2,
            new BatchPreparedStatementSetter() {
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                AnimeGenreParam param = genreParams.get(i);
                ps.setLong(1, param.getAnimeId());
                ps.setString(2, param.getGenre());
              }

              public int getBatchSize() {
                return genreParams.size();
              }
            });
  }

  public static class AnimeGenreParam {
    private final Long animeId;
    private final String genre;

    public AnimeGenreParam(Long animeId, String genre) {
      this.animeId = animeId;
      this.genre = genre;
    }

    public Long getAnimeId() {
      return animeId;
    }

    public String getGenre() {
      return genre;
    }
  }
}

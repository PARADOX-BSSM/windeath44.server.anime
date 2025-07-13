package com.example.anime.domain.anime.repository;
import com.example.anime.domain.anime.model.Anime;
import java.util.List;

public interface JdbcAnimeRepository {
  void bulkInsert(List<Anime> animes);
}

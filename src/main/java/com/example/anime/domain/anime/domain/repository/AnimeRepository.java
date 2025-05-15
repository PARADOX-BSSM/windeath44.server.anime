package com.example.anime.domain.anime.domain.repository;

import com.example.anime.domain.anime.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
  @Query("select a from Anime a join fetch a.tags where a.animeId = :id")
  Optional<Anime> findByIdWithTags(@Param("id") Long animeId);

  @Query("select a from Anime a join fetch a.tags")
  List<Anime> findAllWithTags();

  @Query("select a from Anime a join fetch a.tags where a.animeId > :cursorId order by a.animeId asc limit :size")
  List<Anime> findAllByCursorId(@Param("cursorId") Long cursorId, @Param("size") Long size);
}

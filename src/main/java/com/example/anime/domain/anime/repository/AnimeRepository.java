package com.example.anime.domain.anime.repository;

import com.example.anime.domain.anime.model.Anime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeRepository extends JpaRepository<Anime, Long>, JdbcAnimeRepository {

  @Query("select a from Anime a join fetch a.genres where a.animeId < :cursorId order by a.animeId desc")
  Slice<Anime> findRecentAnimesByCursorId(@Param("cursorId") Long cursorId, Pageable pageable);

  @Query("select a from Anime a join fetch a.genres order by a.animeId desc")
  Slice<Anime> findRecentAnimes(Pageable pageable);

  @Query("select a from Anime a join fetch a.genres where a.name like %:name% order by a.animeId desc")
  Slice<Anime> findRecentAnimesByName(Pageable pageable, @Param("name") String animeName);

  @Query("select a from Anime a join fetch a.genres where a.name like %:name% and a.animeId < :cursorId order by a.animeId desc")
  Slice<Anime> findRecentAnimesByCursorIdAndName(Long cursorId, Pageable pageable, String animeName);

}

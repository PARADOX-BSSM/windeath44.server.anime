package com.example.anime.domain.character.repository;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.character.model.Character;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
  List<Character> findAllByAnime(Anime anime);

  @Query("select c from Character c ")
  List<Character> findAllByAnimeName(String name);
  List<Character> findAllByName(String name);

  @Query("select count(c) from Character c where c.anime.animeId = :animeId")
  Long countBowCountByAnime(@Param("animeId") Long animeId);

  @Query("select c from Character c where c.characterId < :cursorId order by c.characterId desc")
  Slice<Character> findAllByCursorId(Long cursorId, Pageable pageable);

  @Query("select c from Character c order by c.characterId desc")
  Slice<Character> findAllPageable(Pageable pageable);


  @Query("select c.characterId from Character c where c.anime.animeId = :animeId")
  List<Long> findIdsByAnimeId(Long animeId);

  @Query("select c.characterId from Character c where c.deathReason = :deathReason")
  List<Long> findIdsByDeathReason(String deathReason);

  @Query("select c from Character c where c.characterId in :characterIds")
  List<Character> findAllByIds(List<Long> characterIds);

  @Query("select c from Character c where c.name like %:name%")
  Slice<Character> findAllPageableByName(String name, Pageable pageable);

  @Query("select c from Character c where c.name like %:name% and c.characterId < :cursorId order by c.characterId desc")
  Slice<Character> findAllByCursorIdAndName(String name, Long cursorId, Pageable pageable);
}

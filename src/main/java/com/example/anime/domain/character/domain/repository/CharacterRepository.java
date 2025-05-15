package com.example.anime.domain.character.domain.repository;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.character.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
  List<Character> findAllByAnime(Anime anime);

  @Query("select count(c) from Character c where c.anime.animeId = :animeId")
  Long countBowCountByAnime(@Param("animeId") Long animeId);
}

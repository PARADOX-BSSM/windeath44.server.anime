package com.example.anime.domain.character.repository.jpa;

import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.model.type.CauseOfDeath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

  @Query("select c from Character c where c.characterId < :cursorId order by c.characterId desc")
  Slice<Character> findAllByCursorId(Long cursorId, Pageable pageable);

  @Query("select c from Character c order by c.characterId desc")
  Slice<Character> findAllPageable(Pageable pageable);

  @Query("select c from Character c where c.anime.animeId in :animeId order by c.characterId desc")
  Slice<Character> findByAnimeId(List<Long> animeId, Pageable pageable);

  @Query("select c from Character c where c.anime.animeId in :animeId and c.characterId <= :cursorId order by c.characterId desc")
  Slice<Character> findByAnimeIdAndCursorId(List<Long> animeId, Long cursorId, Pageable pageable);

  @Query("select c.characterId from Character c where c.deathReason = :deathReason order by c.characterId desc")
  List<Long> findIdsByDeathReason(String deathReason, Pageable pageable);

  @Query("select c.characterId from Character c where c.deathReason = :deathReason and c.characterId <= :cursorId order by c.characterId desc")
  List<Long> findIdsByDeathReasonAndCursorId(String deathReason, Long cursorId, Pageable pageable);

  @Query("select c from Character c where c.characterId in :characterIds")
  List<Character> findAllByIds(List<Long> characterIds);

  @Query("select c from Character c where c.name like concat('%', :name, '%') order by c.characterId desc")
  Slice<Character> findAllPageableByName(String name, Pageable pageable);

  @Query("select c from Character c where c.name like concat('%', :name, '%') and c.characterId < :cursorId order by c.characterId desc")
  Slice<Character> findAllByCursorIdAndName(String name, Long cursorId, Pageable pageable);

  @Query("select c from Character c where c.deathReason = :deathReason")
  Slice<Character> findAllPageableByDeathReason(CauseOfDeath deathReason, Pageable pageable);

  @Query("select c from Character c where c.characterId <= :cursorId and c.deathReason = :deathReason order by c.characterId desc")
  Slice<Character> findAllByCursorIdAndDeathReason(CauseOfDeath deathReason, Long cursorId, Pageable pageable);

  @Query("SELECT c FROM Character c WHERE  (:cursorId IS NULL OR c.characterId > :cursorId)  AND (:name IS NULL OR c.name like %:name%) AND (:animeId IS NULL OR c.anime.animeId in (:animeId)) AND (:deathReason IS NULL OR c.deathReason = :deathReason)")
  Page<Character> findAllWithCursor(
          @Param("name") String name,
          @Param("animeId") List<Long> animeId,
          @Param("deathReason") CauseOfDeath deathReason,
          @Param("cursorId") Long cursorId,
          Pageable pageable
  );
}

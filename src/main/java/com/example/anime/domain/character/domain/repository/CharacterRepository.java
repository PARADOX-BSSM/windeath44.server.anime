package com.example.anime.domain.character.domain.repository;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.character.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
  List<Character> findAllByAnime(Anime anime);
}

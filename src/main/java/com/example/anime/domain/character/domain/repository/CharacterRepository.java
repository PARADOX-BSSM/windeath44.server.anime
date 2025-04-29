package com.example.anime.domain.character.domain.repository;

import com.example.anime.domain.character.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
}

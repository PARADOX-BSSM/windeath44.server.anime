package com.example.anime.domain.character.service;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.model.AnimeAirDates;
import com.example.anime.domain.character.dto.response.CharacterResponse;
import com.example.anime.domain.character.exception.NotFoundCharacterException;
import com.example.anime.domain.character.mapper.CharacterMapper;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.model.CharacterState;
import com.example.anime.domain.character.repository.CharacterRepository;
import com.example.anime.global.dto.CursorPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CharacterMapper characterMapper;

    @InjectMocks
    private CharacterService characterService;

    private Anime testAnime;
    private Character testCharacter;
    private CharacterResponse testCharacterResponse;

    @BeforeEach
    void setUp() {
        // Setup test anime
        testAnime = Anime.builder()
                .animeId(1L)
                .name("Test Anime")
                .description("Test Description")
                .airDates(new AnimeAirDates(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1)))
                .tags(Arrays.asList("Action", "Adventure"))
                .bowCount(0L)
                .build();

        // Setup test character
        testCharacter = Character.builder()
                .characterId(1L)
                .anime(testAnime)
                .name("Test Character")
                .content("Test Content")
                .lifeTime(100L)
                .deathReason("Test Death Reason")
                .imageUrl("http://test.com/image.jpg")
                .state(CharacterState.NOT_MEMORIALIZING)
                .bowCount(0L)
                .build();

        // Setup test character response
        testCharacterResponse = new CharacterResponse(
                1L,
                "Test Character",
                "Test Content",
                100L,
                "Test Death Reason",
                "http://test.com/image.jpg",
                0L
        );
    }

    @Test
    @DisplayName("findById should return character when character exists")
    void findById_ShouldReturnCharacter_WhenCharacterExists() {
        // Arrange
        when(characterRepository.findById(1L)).thenReturn(Optional.of(testCharacter));

        // Act
        Character result = characterService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCharacter.getCharacterId(), result.getCharacterId());
        assertEquals(testCharacter.getName(), result.getName());
        verify(characterRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById should throw NotFoundCharacterException when character does not exist")
    void findById_ShouldThrowException_WhenCharacterDoesNotExist() {
        // Arrange
        when(characterRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundCharacterException.class, () -> characterService.findById(999L));
        verify(characterRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("findAllByAnime should return list of characters for given anime")
    void findAllByAnime_ShouldReturnCharacterList() {
        // Arrange
        List<Character> characterList = Arrays.asList(testCharacter);
        when(characterRepository.findAllByAnime(testAnime)).thenReturn(characterList);

        // Act
        List<Character> result = characterService.findAllByAnime(testAnime);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCharacter.getCharacterId(), result.get(0).getCharacterId());
        verify(characterRepository, times(1)).findAllByAnime(testAnime);
    }

    @Test
    @DisplayName("findAll should return cursor page of character responses")
    void findAll_ShouldReturnCursorPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Character> characterList = Arrays.asList(testCharacter);
        SliceImpl<Character> characterSlice = new SliceImpl<>(characterList, pageable, false);

        when(characterRepository.findAllPageable(any(Pageable.class))).thenReturn(characterSlice);
        when(characterMapper.toCharacterResponse(testCharacter)).thenReturn(testCharacterResponse);

        // Act
        CursorPage<CharacterResponse> result = characterService.findAll(null, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.values().size());
        assertEquals(testCharacterResponse.characterId(), result.values().get(0).characterId());
        assertFalse(result.hasNext());
        verify(characterRepository, times(1)).findAllPageable(any(Pageable.class));
        verify(characterMapper, times(1)).toCharacterResponse(testCharacter);
    }

    @Test
    @DisplayName("findAll with cursorId should return cursor page of character responses")
    void findAll_WithCursorId_ShouldReturnCursorPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Character> characterList = Arrays.asList(testCharacter);
        SliceImpl<Character> characterSlice = new SliceImpl<>(characterList, pageable, true);

        when(characterRepository.findAllByCursorId(eq(5L), any(Pageable.class))).thenReturn(characterSlice);
        when(characterMapper.toCharacterResponse(testCharacter)).thenReturn(testCharacterResponse);

        // Act
        CursorPage<CharacterResponse> result = characterService.findAll(5L, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.values().size());
        assertEquals(testCharacterResponse.characterId(), result.values().get(0).characterId());
        assertTrue(result.hasNext());
        verify(characterRepository, times(1)).findAllByCursorId(eq(5L), any(Pageable.class));
        verify(characterMapper, times(1)).toCharacterResponse(testCharacter);
    }

    @Test
    @DisplayName("find should return character response when character exists")
    void find_ShouldReturnCharacterResponse_WhenCharacterExists() {
        // Arrange
        when(characterRepository.findById(1L)).thenReturn(Optional.of(testCharacter));
        when(characterMapper.toCharacterResponse(testCharacter)).thenReturn(testCharacterResponse);

        // Act
        CharacterResponse result = characterService.find(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCharacterResponse.characterId(), result.characterId());
        assertEquals(testCharacterResponse.name(), result.name());
        verify(characterRepository, times(1)).findById(1L);
        verify(characterMapper, times(1)).toCharacterResponse(testCharacter);
    }

    @Test
    @DisplayName("create should save character")
    void create_ShouldSaveCharacter() {
        // Arrange
        when(characterMapper.toCharacter(
                eq(testAnime),
                eq("Test Character"),
                eq("Test Content"),
                eq("Test Death Reason"),
                eq(100L),
                eq("http://test.com/image.jpg")
        )).thenReturn(testCharacter);

        // Act
        characterService.create(
                testAnime,
                "Test Character",
                "Test Content",
                "Test Death Reason",
                100L,
                "http://test.com/image.jpg"
        );

        // Assert
        verify(characterMapper, times(1)).toCharacter(
                eq(testAnime),
                eq("Test Character"),
                eq("Test Content"),
                eq("Test Death Reason"),
                eq(100L),
                eq("http://test.com/image.jpg")
        );
        verify(characterRepository, times(1)).save(testCharacter);
    }

    @Test
    @DisplayName("memorializing should change character state to MEMORIALIZING")
    void memorializing_ShouldChangeCharacterState() {
        // Arrange
        when(characterRepository.findById(1L)).thenReturn(Optional.of(testCharacter));

        // Act
        characterService.memorializing(1L);

        // Assert
        assertEquals(CharacterState.MEMORIALIZING, testCharacter.getState());
        verify(characterRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findIdsByAnime should return list of character ids for given anime id")
    void findIdsByAnime_ShouldReturnCharacterIdList() {
        // Arrange
        List<Long> characterIds = Arrays.asList(1L, 2L, 3L);
        when(characterRepository.findIdsByAnimeId(1L)).thenReturn(characterIds);

        // Act
        List<Long> result = characterService.findIdsByAnime(1L);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(characterIds, result);
        verify(characterRepository, times(1)).findIdsByAnimeId(1L);
    }

    @Test
    @DisplayName("findIdsByDeathReason should return list of character ids for given death reason")
    void findIdsByDeathReason_ShouldReturnCharacterIdList() {
        // Arrange
        List<Long> characterIds = Arrays.asList(1L, 2L, 3L);
        when(characterRepository.findIdsByDeathReason("Test Death Reason")).thenReturn(characterIds);

        // Act
        List<Long> result = characterService.findIdsByDeathReason("Test Death Reason");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(characterIds, result);
        verify(characterRepository, times(1)).findIdsByDeathReason("Test Death Reason");
    }
}

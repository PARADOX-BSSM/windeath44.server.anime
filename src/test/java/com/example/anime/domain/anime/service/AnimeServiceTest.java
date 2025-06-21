package com.example.anime.domain.anime.service;

import com.example.anime.domain.anime.dto.response.AnimeListResponse;
import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.exception.NotFoundAnimeException;
import com.example.anime.domain.anime.mapper.AnimeMapper;
import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.model.AnimeAirDates;
import com.example.anime.domain.anime.repository.AnimeRepository;
import com.example.anime.domain.character.model.Character;
import com.example.anime.domain.character.service.CharacterService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private AnimeMapper animeMapper;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private AnimeService animeService;

    private Anime testAnime;
    private AnimeAirDates testAnimeAirDates;
    private AnimeListResponse testAnimeListResponse;
    private AnimeResponse testAnimeResponse;
    private List<Character> testCharacterList;

    @BeforeEach
    void setUp() {
        // Setup test anime air dates
        testAnimeAirDates = new AnimeAirDates(
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1)
        );

        // Setup test anime
        testAnime = Anime.builder()
                .animeId(1L)
                .name("Test Anime")
                .description("Test Description")
                .airDates(testAnimeAirDates)
                .tags(Arrays.asList("Action", "Adventure"))
                .bowCount(0L)
                .build();

        // Setup test anime list response
        testAnimeListResponse = new AnimeListResponse(
                1L,
                "Test Anime",
                "Test Description",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1),
                0L,
                Arrays.asList("Action", "Adventure"),
                0L,
                "http://test.com/image.jpg"
        );

        // Setup test character list
        testCharacterList = Arrays.asList(
                Character.builder()
                        .characterId(1L)
                        .anime(testAnime)
                        .name("Test Character")
                        .build()
        );

        // Setup test anime response
        testAnimeResponse = new AnimeResponse(
                "Test Anime",
                "Test Description",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1),
                0L,
                Arrays.asList("Action", "Adventure"),
                0L,
                Arrays.asList(),
                "http://test.com/image.jpg"
        );
    }

    @Test
    @DisplayName("create should save anime")
    void create_ShouldSaveAnime() {
        // Arrange
        String name = "Test Anime";
        String description = "Test Description";
        LocalDate startYear = LocalDate.of(2020, 1, 1);
        LocalDate endYear = LocalDate.of(2021, 1, 1);
        List<String> tags = Arrays.asList("Action", "Adventure");

        when(animeMapper.toAnime(eq(name), eq(description), any(AnimeAirDates.class), eq(tags)))
                .thenReturn(testAnime);

        // Act
        animeService.create(name, description, startYear, endYear, tags);

        // Assert
        verify(animeMapper, times(1)).toAnime(eq(name), eq(description), any(AnimeAirDates.class), eq(tags));
        verify(animeRepository, times(1)).save(testAnime);
    }

    @Test
    @DisplayName("delete should delete anime when anime exists")
    void delete_ShouldDeleteAnime_WhenAnimeExists() {
        // Arrange
        when(animeRepository.findByIdWithTagsAAndCharacterList(1L)).thenReturn(Optional.of(testAnime));

        // Act
        animeService.delete(1L);

        // Assert
        verify(animeRepository, times(1)).findByIdWithTagsAAndCharacterList(1L);
        verify(animeRepository, times(1)).delete(testAnime);
    }

    @Test
    @DisplayName("delete should throw NotFoundAnimeException when anime does not exist")
    void delete_ShouldThrowException_WhenAnimeDoesNotExist() {
        // Arrange
        when(animeRepository.findByIdWithTagsAAndCharacterList(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundAnimeException.class, () -> animeService.delete(999L));
        verify(animeRepository, times(1)).findByIdWithTagsAAndCharacterList(999L);
        verify(animeRepository, never()).delete(any(Anime.class));
    }

    @Test
    @DisplayName("getAnime should return anime when anime exists")
    void getAnime_ShouldReturnAnime_WhenAnimeExists() {
        // Arrange
        when(animeRepository.findByIdWithTagsAAndCharacterList(1L)).thenReturn(Optional.of(testAnime));

        // Act
        Anime result = animeService.getAnime(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAnime.getAnimeId(), result.getAnimeId());
        assertEquals(testAnime.getName(), result.getName());
        verify(animeRepository, times(1)).findByIdWithTagsAAndCharacterList(1L);
    }

    @Test
    @DisplayName("getAnime should throw NotFoundAnimeException when anime does not exist")
    void getAnime_ShouldThrowException_WhenAnimeDoesNotExist() {
        // Arrange
        when(animeRepository.findByIdWithTagsAAndCharacterList(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundAnimeException.class, () -> animeService.getAnime(999L));
        verify(animeRepository, times(1)).findByIdWithTagsAAndCharacterList(999L);
    }

    @Test
    @DisplayName("findAndRenewAnimeById should return anime and renew bow count when anime exists")
    void findAndRenewAnimeById_ShouldReturnAnimeAndRenewBowCount_WhenAnimeExists() {
        // Arrange
        when(animeRepository.findByIdWithTagsAAndCharacterList(1L)).thenReturn(Optional.of(testAnime));

        // Act
        Anime result = animeService.findAndRenewAnimeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAnime.getAnimeId(), result.getAnimeId());
        assertEquals(testAnime.getName(), result.getName());
        verify(animeRepository, times(1)).findByIdWithTagsAAndCharacterList(1L);
    }

    @Test
    @DisplayName("findAll should return list of anime list responses")
    void findAll_ShouldReturnAnimeListResponses() {
        // Arrange
        List<Anime> animeList = Arrays.asList(testAnime);
        when(animeRepository.findAllWithTagsAndCharacterList()).thenReturn(animeList);
        when(animeMapper.toAnimeListResponse(testAnime)).thenReturn(testAnimeListResponse);

        // Act
        List<AnimeListResponse> result = animeService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAnimeListResponse.animeId(), result.get(0).animeId());
        verify(animeRepository, times(1)).findAllWithTagsAndCharacterList();
        verify(animeMapper, times(1)).toAnimeListResponse(testAnime);
    }

    @Test
    @DisplayName("findById should return anime response when anime exists")
    void findById_ShouldReturnAnimeResponse_WhenAnimeExists() {
        // Arrange
        when(animeRepository.findByIdWithTagsAAndCharacterList(1L)).thenReturn(Optional.of(testAnime));
        when(characterService.findAllByAnime(testAnime)).thenReturn(testCharacterList);
        when(animeMapper.toAnimeResponse(testAnime, testCharacterList)).thenReturn(testAnimeResponse);

        // Act
        AnimeResponse result = animeService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAnimeResponse.name(), result.name());
        assertEquals(testAnimeResponse.description(), result.description());
        verify(animeRepository, times(1)).findByIdWithTagsAAndCharacterList(1L);
        verify(characterService, times(1)).findAllByAnime(testAnime);
        verify(animeMapper, times(1)).toAnimeResponse(testAnime, testCharacterList);
    }

    @Test
    @DisplayName("update should update anime when anime exists")
    void update_ShouldUpdateAnime_WhenAnimeExists() {
        // Arrange
        String name = "Updated Anime";
        String description = "Updated Description";
        LocalDate startYear = LocalDate.of(2022, 1, 1);
        LocalDate endYear = LocalDate.of(2023, 1, 1);
        List<String> tags = Arrays.asList("Drama", "Fantasy");

        when(animeRepository.findByIdWithTagsAAndCharacterList(1L)).thenReturn(Optional.of(testAnime));

        // Act
        animeService.update(1L, name, description, startYear, endYear, tags);

        // Assert
        verify(animeRepository, times(1)).findByIdWithTagsAAndCharacterList(1L);
    }

    @Test
    @DisplayName("findAllByCursorId with null cursorId should return first page")
    void findAllByCursorId_WithNullCursorId_ShouldReturnFirstPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 11); // size + 1
        List<Anime> animeList = Arrays.asList(testAnime);
        SliceImpl<Anime> animeSlice = new SliceImpl<>(animeList, pageable, false);

        when(animeRepository.findPage(any(Pageable.class))).thenReturn(animeSlice);
        when(animeMapper.toAnimePageListResponse(animeSlice)).thenReturn(Arrays.asList(testAnimeListResponse));

        // Act
        CursorPage<AnimeListResponse> result = animeService.findAllByCursorId(null, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.values().size());
        assertEquals(testAnimeListResponse.name(), result.values().get(0).name());
        assertFalse(result.hasNext());
        verify(animeRepository, times(1)).findPage(any(Pageable.class));
        verify(animeMapper, times(1)).toAnimePageListResponse(animeSlice);
    }

    @Test
    @DisplayName("findAllByCursorId with cursorId should return page after cursor")
    void findAllByCursorId_WithCursorId_ShouldReturnPageAfterCursor() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 11); // size + 1
        List<Anime> animeList = Arrays.asList(testAnime);
        SliceImpl<Anime> animeSlice = new SliceImpl<>(animeList, pageable, true);

        when(animeRepository.findPageByCursorId(eq(5L), any(Pageable.class))).thenReturn(animeSlice);
        when(animeMapper.toAnimePageListResponse(animeSlice)).thenReturn(Arrays.asList(testAnimeListResponse));

        // Act
        CursorPage<AnimeListResponse> result = animeService.findAllByCursorId(5L, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.values().size());
        assertEquals(testAnimeListResponse.name(), result.values().get(0).name());
        assertTrue(result.hasNext());
        verify(animeRepository, times(1)).findPageByCursorId(eq(5L), any(Pageable.class));
        verify(animeMapper, times(1)).toAnimePageListResponse(animeSlice);
    }
}

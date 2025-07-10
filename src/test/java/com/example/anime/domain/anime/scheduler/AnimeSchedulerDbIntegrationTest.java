package com.example.anime.domain.anime.scheduler;

import com.example.anime.domain.anime.dto.response.RestAnimeResponse;
import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.repository.AnimeRepository;
import com.example.anime.global.dto.LaftelResultResponse;
import com.example.anime.global.infrastructure.RestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AnimeSchedulerDbIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public RestHttpClient mockRestHttpClient() {
            return Mockito.mock(RestHttpClient.class);
        }
    }

    @Autowired
    private AnimeScheduler animeScheduler;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private RestHttpClient restHttpClient;

    private LaftelResultResponse testLaftelResultResponse;

    @BeforeEach
    void setUp() {
        // Reset static fields in AnimeScheduler
        ReflectionTestUtils.setField(AnimeScheduler.class, "offset", 0);
        ReflectionTestUtils.setField(AnimeScheduler.class, "cachedId", null);

        // Clean up the database
        animeRepository.deleteAll();

        // Create test RestAnimeResponse objects
        RestAnimeResponse testAnime1 = new RestAnimeResponse(
                1L,
                "Test Anime 1",
                Arrays.asList("Action", "Adventure"),
                "http://test.com/image1.jpg"
        );

        RestAnimeResponse testAnime2 = new RestAnimeResponse(
                2L,
                "Test Anime 2",
                Arrays.asList("Comedy", "Drama"),
                "http://test.com/image2.jpg"
        );

        // Create test LaftelResultResponse
        testLaftelResultResponse = new LaftelResultResponse(
                100L,
                Arrays.asList(testAnime1, testAnime2),
                null
        );

        // Mock the RestHttpClient
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
    }

    @Test
    @DisplayName("loadingAnime should save anime data to the database")
    void loadingAnime_ShouldSaveAnimeDataToDatabase() throws InterruptedException {
        // Act
        animeScheduler.loadingAnime();

        // Assert
        // Verify that the anime data was saved to the database
        List<Anime> savedAnimes = animeRepository.findAll();
        assertEquals(2, savedAnimes.size());

        // Verify first anime
        Optional<Anime> savedAnime1 = animeRepository.findById(1L);
        assertTrue(savedAnime1.isPresent());
        assertEquals("Test Anime 1", savedAnime1.get().getName());
        assertEquals("http://test.com/image1.jpg", savedAnime1.get().getImageUrl());
        assertEquals(2, savedAnime1.get().getGenres().size());
        assertTrue(savedAnime1.get().getGenres().contains("Action"));
        assertTrue(savedAnime1.get().getGenres().contains("Adventure"));

        // Verify second anime
        Optional<Anime> savedAnime2 = animeRepository.findById(2L);
        assertTrue(savedAnime2.isPresent());
        assertEquals("Test Anime 2", savedAnime2.get().getName());
        assertEquals("http://test.com/image2.jpg", savedAnime2.get().getImageUrl());
        assertEquals(2, savedAnime2.get().getGenres().size());
        assertTrue(savedAnime2.get().getGenres().contains("Comedy"));
        assertTrue(savedAnime2.get().getGenres().contains("Drama"));

        // Verify that the RestHttpClient was called
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
    }

    @Test
    @DisplayName("recursiveLoadingAnime should save all anime data to the database")
    void recursiveLoadingAnime_ShouldSaveAllAnimeDataToDatabase() throws InterruptedException {
        // Arrange
        // Create second page of results
        RestAnimeResponse testAnime3 = new RestAnimeResponse(
                3L,
                "Test Anime 3",
                Arrays.asList("Horror", "Mystery"),
                "http://test.com/image3.jpg"
        );

        RestAnimeResponse testAnime4 = new RestAnimeResponse(
                4L,
                "Test Anime 4",
                Arrays.asList("Sci-Fi", "Fantasy"),
                "http://test.com/image4.jpg"
        );

        LaftelResultResponse secondPageResponse = new LaftelResultResponse(
                100L,
                Arrays.asList(testAnime3, testAnime4),
                null
        );

        // Mock first page with next page available
        LaftelResultResponse firstPageResponse = new LaftelResultResponse(
                100L,
                Arrays.asList(testLaftelResultResponse.results().get(0), testLaftelResultResponse.results().get(1)),
                "http://test.com/next"
        );

        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(firstPageResponse);
        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(secondPageResponse);

        // Act
        animeScheduler.recursiveLoadingAnime();

        // Assert
        // Verify that all anime data was saved to the database
        List<Anime> savedAnimes = animeRepository.findAll();
        assertEquals(4, savedAnimes.size());

        // Verify anime 3
        Optional<Anime> savedAnime3 = animeRepository.findById(3L);
        assertTrue(savedAnime3.isPresent());
        assertEquals("Test Anime 3", savedAnime3.get().getName());
        assertEquals("http://test.com/image3.jpg", savedAnime3.get().getImageUrl());
        assertEquals(2, savedAnime3.get().getGenres().size());
        assertTrue(savedAnime3.get().getGenres().contains("Horror"));
        assertTrue(savedAnime3.get().getGenres().contains("Mystery"));

        // Verify anime 4
        Optional<Anime> savedAnime4 = animeRepository.findById(4L);
        assertTrue(savedAnime4.isPresent());
        assertEquals("Test Anime 4", savedAnime4.get().getName());
        assertEquals("http://test.com/image4.jpg", savedAnime4.get().getImageUrl());
        assertEquals(2, savedAnime4.get().getGenres().size());
        assertTrue(savedAnime4.get().getGenres().contains("Sci-Fi"));
        assertTrue(savedAnime4.get().getGenres().contains("Fantasy"));

        // Verify that the RestHttpClient was called for both pages
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 100);
    }
}

package com.example.anime.domain.anime.scheduler;

import com.example.anime.domain.anime.dto.response.RestAnimeResponse;
import com.example.anime.domain.anime.exception.AlreadyCachedAnimeException;
import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.repository.AnimeRepository;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.global.dto.LaftelResultResponse;
import com.example.anime.global.infrastructure.RestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimeSchedulerIntegrationTest {

    @Mock
    private AnimeService animeService;

    @Mock
    private RestHttpClient restHttpClient;

    @InjectMocks
    private AnimeScheduler animeScheduler;

    private LaftelResultResponse testLaftelResultResponse;
    private LaftelResultResponse testLaftelResultResponseWithCachedId;
    private LaftelResultResponse testLaftelResultResponseEnd;

    @BeforeEach
    void setUp() {
        // Reset static fields in AnimeScheduler
        ReflectionTestUtils.setField(AnimeScheduler.class, "offset", 0);
        ReflectionTestUtils.setField(AnimeScheduler.class, "cachedId", null);

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

        // Create test LaftelResultResponse with more pages
        testLaftelResultResponse = new LaftelResultResponse(
                100L,
                Arrays.asList(testAnime1, testAnime2),
                "http://test.com/next"
        );

        // Create test LaftelResultResponse with cached ID
        testLaftelResultResponseWithCachedId = new LaftelResultResponse(
                100L,
                Arrays.asList(testAnime1, testAnime2),
                "http://test.com/next"
        );

        // Create test LaftelResultResponse with no more pages
        testLaftelResultResponseEnd = new LaftelResultResponse(
                100L,
                Arrays.asList(testAnime1, testAnime2),
                null
        );
    }

    @Test
    @DisplayName("loadingAnime should load anime data and save to database")
    void loadingAnime_ShouldLoadAnimeDataAndSaveToDatabase() throws InterruptedException {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);

        // Act
        boolean result = animeScheduler.loadingAnime();

        // Assert
        assertTrue(result);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(animeService, times(1)).save(testLaftelResultResponse);
        assertEquals(100, ReflectionTestUtils.getField(animeScheduler, "offset"));
    }

    @Test
    @DisplayName("loadingAnime should return false when there are no more pages")
    void loadingAnime_ShouldReturnFalse_WhenNoMorePages() throws InterruptedException {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponseEnd);

        // Act
        boolean result = animeScheduler.loadingAnime();

        // Assert
        assertFalse(result);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(animeService, times(1)).save(testLaftelResultResponseEnd);
        assertEquals(100, ReflectionTestUtils.getField(animeScheduler, "offset"));
    }

    @Test
    @DisplayName("loadingAnime should throw AlreadyCachedAnimeException when cached anime is found")
    void loadingAnime_ShouldThrowException_WhenCachedAnimeIsFound() throws InterruptedException {
        // Arrange
        // First call to set cachedId
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
        animeScheduler.loadingAnime();

        // Second call with cached ID
        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(testLaftelResultResponseWithCachedId);
        
        // Mock containsCachedId to return true
        Long cachedId = (Long) ReflectionTestUtils.getField(AnimeScheduler.class, "cachedId");
        doReturn(true).when(testLaftelResultResponseWithCachedId).containsCachedId(cachedId);

        // Act & Assert
        assertThrows(AlreadyCachedAnimeException.class, () -> animeScheduler.loadingAnime());
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 100);
        verify(animeService, never()).save(testLaftelResultResponseWithCachedId);
    }

    @Test
    @DisplayName("recursiveLoadingAnime should load anime data until end or exception")
    void recursiveLoadingAnime_ShouldLoadAnimeDataUntilEndOrException() throws InterruptedException {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(testLaftelResultResponseEnd);

        // Act
        animeScheduler.recursiveLoadingAnime();

        // Assert
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 100);
        verify(animeService, times(2)).save(any(LaftelResultResponse.class));
    }

    @Test
    @DisplayName("recursiveLoadingAnime should handle AlreadyCachedAnimeException")
    void recursiveLoadingAnime_ShouldHandleAlreadyCachedAnimeException() throws InterruptedException {
        // Arrange
        // First call to set cachedId
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
        
        // Second call with cached ID
        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(testLaftelResultResponseWithCachedId);
        
        // Mock containsCachedId to return true on second call
        doReturn(false).when(testLaftelResultResponseWithCachedId).containsCachedId(null);
        doReturn(true).when(testLaftelResultResponseWithCachedId).containsCachedId(1L);

        // Act
        animeScheduler.recursiveLoadingAnime();

        // Assert
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 100);
        verify(animeService, times(1)).save(testLaftelResultResponse);
    }
}
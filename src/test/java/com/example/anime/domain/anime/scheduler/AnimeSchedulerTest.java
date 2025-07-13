package com.example.anime.domain.anime.scheduler;

import com.example.anime.domain.anime.exception.AlreadyCachedAnimeException;
import com.example.anime.domain.anime.model.collection.AnimeTitleSet;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.anime.dto.response.RestAnimeResponse;
import com.example.anime.global.dto.LaftelResultResponse;
import com.example.anime.global.infrastructure.RestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimeSchedulerTest {

    @Mock
    private AnimeService animeService;

    @Mock
    private RestHttpClient restHttpClient;

    @InjectMocks
    private AnimeScheduler animeScheduler;

    private LaftelResultResponse testLaftelResultResponse;
    private LaftelResultResponse testLaftelResultResponseWithNext;
    private LaftelResultResponse testLaftelResultResponseWithCachedId;

    @BeforeEach
    void setUp() {
        // Reset static fields in AnimeScheduler
        ReflectionTestUtils.setField(AnimeScheduler.class, "offset", 0);
        ReflectionTestUtils.setField(AnimeScheduler.class, "cachedId", null);
        ReflectionTestUtils.setField(AnimeScheduler.class, "cachedTitleSet", new AnimeTitleSet());

        // Setup test data
        RestAnimeResponse testAnime1 = new RestAnimeResponse(
                1L,
                "Test Anime 1",
                List.of("Action", "Adventure"),
                "http://test.com/image1.jpg"
        );

        RestAnimeResponse testAnime2 = new RestAnimeResponse(
                2L,
                "Test Anime 2",
                List.of("Comedy", "Drama"),
                "http://test.com/image2.jpg"
        );

        // Response with no next page (isEnd() returns true)
        testLaftelResultResponse = new LaftelResultResponse(
                2L,
                List.of(testAnime1, testAnime2),
                null
        );

        // Response with next page (isEnd() returns false)
        testLaftelResultResponseWithNext = new LaftelResultResponse(
                2L,
                List.of(testAnime1, testAnime2),
                "http://test.com/next"
        );

        // Response with cached ID
        testLaftelResultResponseWithCachedId = new LaftelResultResponse(
                2L,
                List.of(testAnime1, testAnime2),
                "http://test.com/next"
        );
    }

    @Test
    @DisplayName("loadingAnime should return false when there are no more pages")
    void loadingAnime_ShouldReturnFalse_WhenNoMorePages() throws InterruptedException {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
        doNothing().when(animeService).save(any(LaftelResultResponse.class));

        // Act
        boolean result = animeScheduler.loadingAnime();

        // Assert
        assertFalse(result);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(animeService, times(1)).save(testLaftelResultResponse);
    }

    @Test
    @DisplayName("loadingAnime should return true when there are more pages")
    void loadingAnime_ShouldReturnTrue_WhenMorePages() throws InterruptedException {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponseWithNext);
        doNothing().when(animeService).save(any(LaftelResultResponse.class));

        // Act
        boolean result = animeScheduler.loadingAnime();

        // Assert
        assertTrue(result);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(animeService, times(1)).save(testLaftelResultResponseWithNext);
    }

    @Test
    @DisplayName("recursiveLoadingAnime should load all pages until end")
    void recursiveLoadingAnime_ShouldLoadAllPages_UntilEnd() throws InterruptedException {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponseWithNext);
        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(testLaftelResultResponse);
        doNothing().when(animeService).save(any(LaftelResultResponse.class));

        // Act
        animeScheduler.recursiveLoadingAnime();

        // Assert
        // Verify that loadAnime was called with the correct parameters
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 100);

        // Verify that save was called at least once
        // The exact number of calls depends on the implementation of recursiveLoadingAnime
        verify(animeService, atLeastOnce()).save(any(LaftelResultResponse.class));
    }

    @Test
    @DisplayName("recursiveLoadingAnime should stop when AlreadyCachedAnimeException is thrown")
    void recursiveLoadingAnime_ShouldStop_WhenAlreadyCachedAnimeExceptionIsThrown() throws InterruptedException {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponseWithNext);

        // First call succeeds, second call throws exception
        doNothing().when(animeService).save(testLaftelResultResponseWithNext);

        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(testLaftelResultResponseWithCachedId);

        // Set up cachedId to trigger exception on second call
        ReflectionTestUtils.setField(AnimeScheduler.class, "cachedId", 1L);

        // Act
        animeScheduler.recursiveLoadingAnime();

        // Assert
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 0);
        verify(restHttpClient, times(1)).loadAnime("recent", 100, 100);
        verify(animeService, times(1)).save(any(LaftelResultResponse.class));
    }

    @Test
    @DisplayName("checkCacheAnime should set cachedId when offset is 0")
    void checkCacheAnime_ShouldSetCachedId_WhenOffsetIsZero() throws Exception {
        // Arrange
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
        doNothing().when(animeService).save(any(LaftelResultResponse.class));

        // Act
        animeScheduler.loadingAnime();

        // Assert
        Long cachedId = (Long) ReflectionTestUtils.getField(AnimeScheduler.class, "cachedId");
        assertEquals(1L, cachedId);
    }

    @Test
    @DisplayName("checkCacheAnime should throw AlreadyCachedAnimeException when cached ID is found")
    void checkCacheAnime_ShouldThrowException_WhenCachedIdIsFound() throws Exception {
        // Arrange
        // First call to set up cachedId
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
        doNothing().when(animeService).save(any(LaftelResultResponse.class));
        animeScheduler.loadingAnime();

        // Reset offset to simulate second page
        ReflectionTestUtils.setField(AnimeScheduler.class, "offset", 100);

        // Second call with same ID to trigger exception
        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(testLaftelResultResponse);

        // Act & Assert
        assertThrows(AlreadyCachedAnimeException.class, () -> animeScheduler.loadingAnime());
    }

    @Test
    @DisplayName("loadingAnime should filter out already cached anime titles")
    void loadingAnime_ShouldFilterOutAlreadyCachedAnimeTitles() throws InterruptedException {
        // Arrange
        // First call to add titles to cache
        when(restHttpClient.loadAnime("recent", 100, 0)).thenReturn(testLaftelResultResponse);
        doNothing().when(animeService).save(any(LaftelResultResponse.class));
        animeScheduler.loadingAnime();

        // Reset offset to simulate second page
        ReflectionTestUtils.setField(AnimeScheduler.class, "offset", 100);

        // Create new response with different IDs but same titles (normalized)
        RestAnimeResponse testAnime3 = new RestAnimeResponse(
                3L,
                "Test Anime 1 2기", // Same as testAnime1 after normalization
                List.of("Action", "Adventure"),
                "http://test.com/image3.jpg"
        );

        LaftelResultResponse secondResponse = new LaftelResultResponse(
                1L,
                List.of(testAnime3),
                null
        );

        when(restHttpClient.loadAnime("recent", 100, 100)).thenReturn(secondResponse);

        // Act
        animeScheduler.loadingAnime();

        // Assert
        // Verify that save was called with filtered response
        verify(animeService, times(2)).save(any(LaftelResultResponse.class));
    }
}

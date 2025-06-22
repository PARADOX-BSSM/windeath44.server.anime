package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.anime.model.Anime;
import com.example.anime.domain.anime.model.AnimeAirDates;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.dto.request.CharacterRequest;
import com.example.anime.domain.character.exception.UploadFileFailException;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.storage.FileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCharacterUseCaseTest {

    @Mock
    private AnimeService animeService;

    @Mock
    private CharacterService characterService;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private CreateCharacterUseCase createCharacterUseCase;

    private Anime testAnime;
    private CharacterRequest testCharacterRequest;
    private MultipartFile testImage;
    private CharacterRequest characterRequest;

    @BeforeEach
    void setUp() {
        // Setup test anime
        characterRequest = new CharacterRequest(1L, "나루토", "죽다", 4L, "그냥");
        testAnime = Anime.builder()
                .animeId(1L)
                .name("Test Anime")
                .description("Test Description")
                .airDates(new AnimeAirDates(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1)))
                .tags(Arrays.asList("Action", "Adventure"))
                .bowCount(0L)
                .build();

        // Setup test image
        testImage = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

    }

    @Test
    @DisplayName("execute should create character when all inputs are valid")
    void execute_ShouldCreateCharacter_WhenAllInputsAreValid() throws IOException {
        // Arrange
        when(fileStorage.upload("1", testImage)).thenReturn("http://test.com/image.jpg");
        when(animeService.getAnime(1L)).thenReturn(testAnime);

        // Act
        createCharacterUseCase.execute(testCharacterRequest);

        // Assert
        verify(fileStorage, times(1)).upload("1", testImage);
        verify(animeService, times(1)).getAnime(1L);
        verify(characterService, times(1)).create(
                characterRequest,
                testAnime
        );
    }

    @Test
    @DisplayName("execute should throw UploadFileFailException when file upload fails")
    void execute_ShouldThrowException_WhenFileUploadFails() throws IOException {
        // Arrange
        when(fileStorage.upload("1", testImage)).thenThrow(new IOException("Upload failed"));

        // Act & Assert
        assertThrows(UploadFileFailException.class, () -> createCharacterUseCase.execute(testCharacterRequest));
        verify(fileStorage, times(1)).upload("1", testImage);
        verify(animeService, never()).getAnime(anyLong());
        verify(characterService, never()).create(any(), any());
    }
}
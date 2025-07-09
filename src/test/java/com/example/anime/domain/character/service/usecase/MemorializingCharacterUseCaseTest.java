package com.example.anime.domain.character.service.usecase;

import com.example.anime.domain.character.exception.NotFoundCharacterException;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.global.infrastructure.KafkaProducer;
import com.example.avro.CharacterAvroSchema;
import com.example.avro.MemorialAvroSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemorializingCharacterUseCaseTest {

    @Mock
    private CharacterService characterService;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private MemorializingCharacterUseCase memorializingCharacterUseCase;

    private MemorialAvroSchema testMemorialAvroSchema;
    private CharacterAvroSchema testCharacterAvroSchema;

    @BeforeEach
    void setUp() {
        // Setup test memorial avro schema
        testMemorialAvroSchema = new MemorialAvroSchema();
        testMemorialAvroSchema.setMemorialId("memorial-123");
        testMemorialAvroSchema.setWriterId("writer-123");
        testMemorialAvroSchema.setContent("Test Memorial Content");
        testMemorialAvroSchema.setCharacterId(1L);

        // Setup test character avro schema
        testCharacterAvroSchema = new CharacterAvroSchema();
        testCharacterAvroSchema.setCharacterId(1L);
        testCharacterAvroSchema.setName("Test Character");
        testCharacterAvroSchema.setContent("Test Content");
        testCharacterAvroSchema.setDeathReason("Test Death Reason");
        testCharacterAvroSchema.setState("NOT_MEMORIALIZING");
        testCharacterAvroSchema.setApplicantId("writer-123");
    }

    @Test
    @DisplayName("memorializing should process memorial and send success message when successful")
    void memorializing_ShouldProcessMemorialAndSendSuccessMessage_WhenSuccessful() {
        // Arrange
        when(characterService.transformSchema(testMemorialAvroSchema)).thenReturn(testCharacterAvroSchema);
        doNothing().when(characterService).memorializing(1L);
        doNothing().when(kafkaProducer).send(anyString(), any());

        // Act
        memorializingCharacterUseCase.memorializing(testMemorialAvroSchema);

        // Assert
        verify(characterService, times(1)).transformSchema(testMemorialAvroSchema);
        verify(characterService, times(1)).memorializing(1L);
        verify(kafkaProducer, times(1)).send(eq("character-memorialized-response"), any());
        verify(kafkaProducer, never()).send(eq("character-memorializing-fail-response"), any());
    }

    @Test
    @DisplayName("memorializing should send failure message when exception occurs")
    void memorializing_ShouldSendFailureMessage_WhenExceptionOccurs() {
        // Arrange
        when(characterService.transformSchema(testMemorialAvroSchema)).thenReturn(testCharacterAvroSchema);
        doThrow(new NotFoundCharacterException()).when(characterService).memorializing(1L);
        doNothing().when(kafkaProducer).send(anyString(), any());

        // Act
        memorializingCharacterUseCase.memorializing(testMemorialAvroSchema);

        // Assert
        verify(characterService, times(1)).transformSchema(testMemorialAvroSchema);
        verify(characterService, times(1)).memorializing(1L);
        verify(kafkaProducer, never()).send(eq("character-memorialized-response"), any());
        verify(kafkaProducer, times(1)).send(eq("character-memorializing-fail-response"), any());
    }
}

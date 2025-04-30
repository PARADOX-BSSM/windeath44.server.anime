package com.example.anime.domain.gRPC.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.mapper.CharacterMapper;
import com.example.anime.domain.character.domain.repository.CharacterRepository;
import com.example.grpc.CreateCharacterRequest;
import com.example.grpc.CreateCharacterResponse;
import com.example.grpc.CreateCharacterServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CreateCharacterService extends CreateCharacterServiceGrpc.CreateCharacterServiceImplBase {
  private final CharacterRepository characterRepository;
  private final AnimeService animeService;
  private final CharacterMapper characterMapper;

  @Override
  public void createCharacter(CreateCharacterRequest request, StreamObserver<CreateCharacterResponse> responseObserver) {
    Long animeId = request.getAnimeId();
    String name = request.getName();
    String content = request.getContent();
    String deathReason = request.getDeathReason();
    Long lifeTime = request.getLifeTime();

    Long characterId = saveCharacter(animeId, name, content, deathReason, lifeTime);
    CreateCharacterResponse response = CreateCharacterResponse.newBuilder()
            .setCharacterId(characterId)
                    .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private Long saveCharacter(Long animeId, String name, String content, String deathReason, Long lifeTime) {
    Anime anime = animeService.getAnime(animeId);
    Character character = characterMapper.toCharacter(anime, name, content, deathReason, lifeTime);
    characterRepository.save(character);
    return character.getCharacterId();
  }
}

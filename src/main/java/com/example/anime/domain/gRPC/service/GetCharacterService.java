package com.example.anime.domain.gRPC.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.mapper.CharacterMapper;
import com.example.anime.domain.character.domain.repository.CharacterRepository;
import com.example.anime.domain.character.service.CharacterService;
import com.example.anime.domain.character.service.exception.NotFoundCharacterException;
import com.example.grpc.GetCharacterRequest;
import com.example.grpc.GetCharacterResponse;
import com.example.grpc.GetCharacterServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GetCharacterService extends GetCharacterServiceGrpc.GetCharacterServiceImplBase {
  private final CharacterService characterService;
  private final CharacterMapper characterMapper;

  @Override
  public void getCharacter(GetCharacterRequest request, StreamObserver<GetCharacterResponse> responseObserver) {
    Long characterId = request.getCharacterId();
    try {
      Character character = characterService.findById(characterId);
      GetCharacterResponse getCharacterResponse = characterMapper.toGetCharacterResponse(character);

      responseObserver.onNext(getCharacterResponse);
      responseObserver.onCompleted();

    } catch (NotFoundCharacterException e) {
      responseObserver.onError(Status.NOT_FOUND
              .withDescription(e.getMessage())
              .asRuntimeException());
    }

  }
}

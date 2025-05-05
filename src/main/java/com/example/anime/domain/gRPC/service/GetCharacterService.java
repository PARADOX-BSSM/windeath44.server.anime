package com.example.anime.domain.gRPC.service;

import com.example.anime.domain.anime.domain.Anime;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.domain.character.domain.Character;
import com.example.anime.domain.character.domain.mapper.CharacterMapper;
import com.example.anime.domain.character.domain.repository.CharacterRepository;
import com.example.grpc.CreateCharacterRequest;
import com.example.grpc.CreateCharacterResponse;
import com.example.grpc.CreateCharacterServiceGrpc;
import com.example.grpc.GetCharacterRequest;
import com.example.grpc.GetCharacterResponse;
import com.example.grpc.GetCharacterServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GetCharacterService extends GetCharacterServiceGrpc.GetCharacterServiceImplBase {
  private final CharacterRepository characterRepository;
  private final AnimeService animeService;
  private final CharacterMapper characterMapper;

  @Override
  public void getCharacter(GetCharacterRequest request, StreamObserver<GetCharacterResponse> responseObserver) {
    Long characterId = request.getCharacterId();


  }
}

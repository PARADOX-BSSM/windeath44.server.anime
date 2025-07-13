package com.example.anime.global.infrastructure;

import com.example.anime.global.dto.LaftelResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class RestHttpClient {
  private final RestClient restClient;

  public LaftelResultResponse loadAnime(String sort, int size, int offset) {
    String path = "/search/v1/discover/";

    LaftelResultResponse laftelResultResponseResponse = restClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path(path)
                    .queryParam("sort", sort)
                    .queryParam("size", size)
                    .queryParam("offset", offset)
                    .build())
            .retrieve()
            .body(LaftelResultResponse.class);
    return laftelResultResponseResponse;

  }
}

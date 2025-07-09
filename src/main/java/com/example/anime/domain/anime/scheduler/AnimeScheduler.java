package com.example.anime.domain.anime.scheduler;

import com.example.anime.domain.anime.dto.response.AnimeResponse;
import com.example.anime.domain.anime.dto.response.RestAnimeResponse;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.global.dto.LaftelResultResponse;
import com.example.anime.global.infrastructure.RestHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnimeScheduler {
  private final AnimeService animeService;
  private final RestHttpClient restHttpClient;

  private final static String SORT = "recent";
  private final static int SIZE = 10;
  private final static int OFFSET = 0;

  @Scheduled(cron="0 0 6 * * *")
  @Transactional
  public void recursiveLoadingAnime() {
    while(loadingAnime()) {
      log.info("load anime success");
      return;
    }
    log.error("load anime fail");
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean loadingAnime() {
    LaftelResultResponse animeResponse = restHttpClient.loadAnime(SORT, SIZE, OFFSET);

    try {
      animeService.save(animeResponse);
    } catch (Exception e) {
      log.error("failed to save anime", e);
      return false;
    }

    return "null".equals(animeResponse.next());
  }
}

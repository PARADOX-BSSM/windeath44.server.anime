package com.example.anime.domain.anime.scheduler;

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
  private final static String NULL = "null";

  @Scheduled(cron="0 0 6 * * *")
  @Transactional
  public void recursiveLoadingAnime()  {
    int count = 0;
    while(loadingAnime()) {
      log.info("load anime success, count : {}", count++);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean loadingAnime()  {
    try {
      Thread.sleep(1000);
      LaftelResultResponse animeResponse = restHttpClient.loadAnime(SORT, SIZE, OFFSET);
      animeService.save(animeResponse);
      return NULL.equals(animeResponse.next());
    } catch (InterruptedException e) {
      log.error(e.getMessage());
      return false;
    }
  }
}

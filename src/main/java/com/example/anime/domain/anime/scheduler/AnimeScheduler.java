package com.example.anime.domain.anime.scheduler;

import com.example.anime.domain.anime.exception.AlreadyCachedAnimeException;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.global.dto.LaftelResultResponse;
import com.example.anime.global.infrastructure.RestHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnimeScheduler {
  private final AnimeService animeService;
  private final RestHttpClient restHttpClient;

  private final static String SORT = "recent";
  private final static int SIZE = 100;
  private static int offset = 0;
  private static Long cachedId = null;

  @Scheduled(cron="0 0 6 * * *")
  public void recursiveLoadingAnime()  {
    int count = 0;
    try {
      while(loadingAnime()) {
        log.info("load anime success, count : {}", ++count);
      }
    } catch(InterruptedException | AlreadyCachedAnimeException e) {
      log.error(e.getMessage());
    }

  }

  @Transactional
  public boolean loadingAnime() throws InterruptedException {
      Thread.sleep(1000);
      LaftelResultResponse animeResponse = restHttpClient.loadAnime(SORT, SIZE, offset);
      // 캐시되어있는지 확인(이미 로드한적이 있는지 확인)
      checkCacheAnime(animeResponse);
      animeService.save(animeResponse);
      offset += SIZE;
      return !animeResponse.isEnd();
  }

  private void checkCacheAnime(LaftelResultResponse animeResponse) {
    if (offset == 0) cachedId = animeResponse.getFirstId();
    boolean isCachedEnd = animeResponse.containsCachedId(cachedId);
    if (isCachedEnd) {
      throw AlreadyCachedAnimeException.getInstance();
    }
  }
}

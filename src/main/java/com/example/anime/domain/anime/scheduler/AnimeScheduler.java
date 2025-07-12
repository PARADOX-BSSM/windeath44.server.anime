package com.example.anime.domain.anime.scheduler;

import com.example.anime.domain.anime.exception.AlreadyCachedAnimeException;
import com.example.anime.domain.anime.model.collection.AnimeTitleSet;
import com.example.anime.domain.anime.service.AnimeService;
import com.example.anime.global.dto.LaftelResultResponse;
import com.example.anime.global.infrastructure.RestHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

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
  private static AnimeTitleSet cachedTitleSet = new AnimeTitleSet();

  @Scheduled(cron="0 0 6 * * *")
  public void recursiveLoadingAnime()  {
    int count = 0;
    try {
      while(loadingAnime()) {
        offset += SIZE;
        log.info("load anime success, count : {}", ++count);
      }
    } catch(InterruptedException | AlreadyCachedAnimeException e) {
      log.error(e.getMessage());
    }
  }

  @Transactional
  public boolean loadingAnime() throws InterruptedException {
      Thread.sleep(1000);
      LaftelResultResponse animeResponse = fetchData();
      // 캐시되어있는지 확인(이미 로드한적이 있는지 확인)
      checkCacheAnime(animeResponse);
      LaftelResultResponse filterAnimeResponse = filter(animeResponse);
      cachedTitleSet.addTitleAnimes(filterAnimeResponse);
      animeService.save(filterAnimeResponse);
      return !filterAnimeResponse.isEnd();
  }

  private LaftelResultResponse filter(LaftelResultResponse animeResponse) {
    LaftelResultResponse filteredLaftelResultResponse = cachedTitleSet.filter(animeResponse);
    return filteredLaftelResultResponse;
  }

  private LaftelResultResponse fetchData() {
    return restHttpClient.loadAnime(SORT, SIZE, offset);
  }

  private void checkCacheAnime(LaftelResultResponse animeResponse) {

    if (offset == 0) {
      cachedId = animeResponse.getFirstId();
    }
    else {
      boolean isCachedId = animeResponse.containsCachedId(cachedId);
      throwIfAlreadyCached(isCachedId);
    }
  }

  private void throwIfAlreadyCached(Boolean isCachedId) {
    if (isCachedId) {
      throw AlreadyCachedAnimeException.getInstance();
    }
  }


}

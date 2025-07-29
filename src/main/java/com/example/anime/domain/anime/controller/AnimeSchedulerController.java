package com.example.anime.domain.anime.controller;

import com.example.anime.domain.anime.scheduler.AnimeScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animes")
public class AnimeSchedulerController {
    private final AnimeScheduler animeScheduler;

    // schedule 테스트용 엔드포인트
    // 사용자들이 접근하는 API가 아님
    @PostMapping("/schedule")
    public void schedule() {
        animeScheduler.recursiveLoadingAnime();
    }
}

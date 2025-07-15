package com.example.anime.domain.anime.model.collection;

import com.example.anime.domain.anime.dto.response.RestAnimeResponse;
import com.example.anime.global.dto.LaftelResultResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimeTitleSet {
    private Set<String> cachedTitleSet = new HashSet<>();
    private static final Pattern pattern = Pattern.compile(
            "^(\\((?:더빙|자막)(?:,\\s*무삭제)?\\)\\s*|\\((무삭제)\\)\\s*|\\[무삭제\\]\\s*)|\\d+기.*$"
    );

    public void addTitleAnimes(LaftelResultResponse animes) {
        animes.results().forEach(anime -> {
            String title = normalizeTitle(anime.name());
            cachedTitleSet.add(title);
        });
    }

    private String normalizeTitle(String name) {
        Matcher matcher = pattern.matcher(name);
        String normalizeTitle =  matcher.replaceAll("").trim();
        return normalizeTitle;
    }

    public LaftelResultResponse filter(LaftelResultResponse animes) {
        List<RestAnimeResponse> filterdAnimes = filterAnimes(animes);
        return new LaftelResultResponse(animes.count(), filterdAnimes, animes.next());
    }

    private List<RestAnimeResponse> filterAnimes(LaftelResultResponse animes) {
        return animes.results().stream()
                .map(anime -> {
                    String normTitle = normalizeTitle(anime.name());
                    if (cachedTitleSet.contains(normTitle)) {
                        return null;
                    }
                    return new RestAnimeResponse(
                            anime.id(),
                            normTitle,
                            anime.genres(),
                            anime.img()
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }

}

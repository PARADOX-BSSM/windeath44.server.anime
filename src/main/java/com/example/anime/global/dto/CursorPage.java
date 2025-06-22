package com.example.anime.global.dto;

import java.util.List;

public record CursorPage<T> (
        List<T> values,
        Boolean hasNext
) {

}

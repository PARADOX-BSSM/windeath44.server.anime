package com.example.anime.global.dto;

public record ResponseDto<T> (
        String message,
        T data
) {
}

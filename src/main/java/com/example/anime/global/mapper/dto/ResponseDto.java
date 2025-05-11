package com.example.anime.global.mapper.dto;

public record ResponseDto<T> (
        String message,
        T data
) {
}

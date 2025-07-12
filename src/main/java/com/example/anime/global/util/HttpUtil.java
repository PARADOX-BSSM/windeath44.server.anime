package com.example.anime.global.util;

import com.example.anime.global.dto.ResponseDto;

public class HttpUtil {

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(message, data);
    }

    public static <T>ResponseDto<T> success(String message) {
        return new ResponseDto<>(message, null);
    }
}

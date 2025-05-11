package com.example.anime.global.mapper;

import com.example.anime.global.mapper.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

  public <T> ResponseDto<T> toResponseDto(String message, T data) {
    return new ResponseDto<>(message, data);
  }
}

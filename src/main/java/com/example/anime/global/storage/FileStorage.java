package com.example.anime.global.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {
  String upload(MultipartFile file) throws IOException;

  String modify(MultipartFile image);
}

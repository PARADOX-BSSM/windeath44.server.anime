package com.example.anime.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("kafka")
public class KafkaProperties {
  private String bootstrapServers;
  private String schemaRegistryUrl;

  private Consumer consumer;

  @Setter
  @Getter
  public static class Consumer {
    private String autoOffsetReset;
  }
}

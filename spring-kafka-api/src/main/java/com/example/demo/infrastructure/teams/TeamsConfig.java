package com.example.demo.infrastructure.teams;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "teams")
@Data
public class TeamsConfig {

  private String endpoint;

  private String path;

  private long connectTimeout;

  private long readTimeout;

}

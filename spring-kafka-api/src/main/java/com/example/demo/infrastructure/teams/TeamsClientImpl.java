package com.example.demo.infrastructure.teams;

import com.example.demo.application.teams.TeamsClient;
import com.example.demo.common.exception.ApplicationException;
import com.example.demo.common.exception.SystemException;
import java.time.Duration;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class TeamsClientImpl implements TeamsClient {

  private final TeamsConfig teamsConfig;

  private RestTemplate restTemplate;

  @PostConstruct
  public void init() {
    restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(teamsConfig.getConnectTimeout()))
            .setReadTimeout(Duration.ofMillis(teamsConfig.getReadTimeout()))
            .build();
  }

  @Override
  public void sendMessage(String title, String message) {
    try {
      ResponseEntity<String> result
              = restTemplate.postForEntity(teamsConfig.getEndpoint() + teamsConfig.getPath(),
              new MessageCard(title, message),
              String.class);

    } catch (RestClientResponseException e) {
      if (HttpStatus.BAD_REQUEST.value() == e.getRawStatusCode()) {
        throw new ApplicationException(e);
      }

      throw new SystemException(e);

    } catch (RestClientException e) {
      throw new SystemException(e);
    }

  }

}

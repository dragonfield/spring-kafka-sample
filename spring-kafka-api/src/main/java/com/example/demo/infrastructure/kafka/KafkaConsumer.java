package com.example.demo.infrastructure.kafka;

import com.example.demo.application.service.NotificationCommand;
import com.example.demo.application.service.NotificationUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumer {

  private final NotificationUseCase notificationUseCase;

  @KafkaListener(topics = "my-topic")
  public void consume(ConsumerRecord<String, String> record) {
    log.debug("key: " + record.key() + ", value: " + record.value());
    TopicEnvelope envelope = unmarshal(record.value());
    NotificationCommand command = new NotificationCommand(envelope.header.getTimestamp(), envelope.getBody().getText());
    notificationUseCase.handle(command);
  }

  private TopicEnvelope unmarshal(String json) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(json, TopicEnvelope.class);
    } catch (JsonProcessingException e) {
      log.error("unexpected error is occurred.", e);
      throw new RuntimeException(e);
    }
  }

}

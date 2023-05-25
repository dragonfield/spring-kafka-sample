package com.example.demo.presentation;

import static com.example.demo.common.utils.JsonUtils.unmarshalFromJson;

import com.example.demo.application.service.NotificationCommand;
import com.example.demo.application.service.NotificationUseCase;
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
  public void consume(ConsumerRecord<String, String> consumerRecord) {
    log.debug("key: " + consumerRecord.key() + ", value: " + consumerRecord.value());
    TopicEnvelope envelope = unmarshalFromJson(consumerRecord.value(), TopicEnvelope.class);
    NotificationCommand command = new NotificationCommand(envelope.header.getTimestamp(), envelope.getBody().getText());
    notificationUseCase.handle(command);
  }

}

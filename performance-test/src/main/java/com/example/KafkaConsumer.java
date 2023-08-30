package com.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @KafkaListener(topics = "sample", containerFactory = "filterKafkaListenerContainerFactory")
  @Transactional
  public void consume(ConsumerRecord<String, String> consumerRecord) {
    log.debug("key: " + consumerRecord.key() + ", value: " + consumerRecord.value());

    kafkaTemplate.send("transfer", consumerRecord.value());

    log.info("send: " + consumerRecord.offset() + ", value:" + consumerRecord.value());
  }

}

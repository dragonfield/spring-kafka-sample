package com.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
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

        ProducerRecord<String, String> record = new ProducerRecord<>("transfer", consumerRecord.value());
        record.headers().add("foo", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).getBytes());
        kafkaTemplate.send(record);

        log.info("send: " + consumerRecord.offset() + ", value:" + consumerRecord.value());
    }

}

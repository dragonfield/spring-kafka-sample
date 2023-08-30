package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyRecordFilter implements RecordFilterStrategy<String, String> {

  private final ObjectMapper objectMappter;

    @Override
    public boolean filter(ConsumerRecord<String, String> consumerRecord) {
        try {
            MyRecord record = objectMappter.readValue(consumerRecord.value(), MyRecord.class);
            log.info(record.getMessageStatus());

            if ("Accepted".equals(record.getMessageStatus())) {
                return false;
            }

        } catch (JsonProcessingException e) {
            log.warn("unmarshalling is failed.", e);
        }

        return true;
    }
}

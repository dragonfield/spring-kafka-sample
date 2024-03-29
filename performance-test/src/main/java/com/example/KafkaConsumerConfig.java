package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@RequiredArgsConstructor
@Configuration
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final MyRecordFilter myRecordFilter;

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    }

    @Bean(name = "filterKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> filterKafkaListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordFilterStrategy(myRecordFilter);
        factory.setConcurrency(3);
        return factory;
    }

}

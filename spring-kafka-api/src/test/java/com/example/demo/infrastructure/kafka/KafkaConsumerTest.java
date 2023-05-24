package com.example.demo.infrastructure.kafka;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.*;
import static org.mockito.Mockito.*;

import com.example.demo.application.service.NotificationCommand;
import com.example.demo.application.service.NotificationUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaConsumerTest {

  @Autowired
  EmbeddedKafkaBroker broker;

  @MockBean
  NotificationUseCase notificationUseCase;

  private KafkaTemplate<String, String> kafkaTemplate;

  private static final String TOPIC1 = "my-topic";

  @BeforeEach
  void setUp() {
    Map<String, Object> config = KafkaTestUtils.producerProps(broker);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<String, String>(config));
  }

  @Test
  void receive() throws Exception {
    TopicEnvelope envelope = new TopicEnvelope(new TopicHeader("0001", "2023/05/20"),
                                               new TopicBody("Hello World"));
    kafkaTemplate.send(TOPIC1, marshal(envelope));

    await().atMost(10, SECONDS).untilAsserted(
            () -> verify(notificationUseCase)
                    .handle(new NotificationCommand("2023/05/20", "Hello World")));
  }

  private String marshal(TopicEnvelope envelope) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(envelope);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
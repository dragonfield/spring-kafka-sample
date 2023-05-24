package com.example.demo.infrastructure.kafka;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.*;
import static org.mockito.Mockito.*;

import com.example.demo.application.service.NotificationCommand;
import com.example.demo.application.service.NotificationUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
class KafkaConsumerTest {

  @Container
  static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
    registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
    registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
  }

  @MockBean
  NotificationUseCase notificationUseCase;

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  AdminClient adminClient;

  private static final String TOPIC1 = "my-topic";

  @BeforeEach
  void setUp() {
    Properties properties = new Properties();
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
    adminClient = AdminClient.create(properties);
  }

  @AfterEach
  void tearDown() throws Exception {
    adminClient.deleteTopics(List.of(TOPIC1));
    adminClient.close();
  }

  @Test
  void receive() throws Exception {
    TopicEnvelope envelope = new TopicEnvelope(new TopicHeader("0001", "2023/05/20"),
                                               new TopicBody("Hello World"));
    kafkaTemplate.send(TOPIC1, marshal(envelope));

    await().pollDelay(1, SECONDS).atMost(10, SECONDS).untilAsserted(
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
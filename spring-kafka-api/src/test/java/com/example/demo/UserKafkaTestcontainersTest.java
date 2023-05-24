package com.example.demo;

import com.example.demo.infrastructure.kafka.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

//@Testcontainers
//@SpringBootTest
public class UserKafkaTestcontainersTest {

//  @Container
//  static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));
//
//  @DynamicPropertySource
//  static void kafkaProperties(DynamicPropertyRegistry registry) {
//    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
//    registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
//    registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
//    registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
//  }
//
//  @Autowired
//  KafkaAdmin kafkaAdmin;
//
//  @Autowired
//  KafkaTemplate<String, String> template;
//
//  @Autowired
//  private KafkaConsumer userKafkaConsumer;
//
//  @BeforeEach
//  void setUp() throws Exception {
//    kafkaAdmin.createOrModifyTopics(TopicBuilder.name("testTopic").build());
//  }
//
//  @AfterEach
//  void tearDown() throws Exception {
//    kafkaAdmin.describeTopics("testTopic");
//  }
//
//  @Test
//  void test() throws Exception {
//    template.send("testTopic", "Hello");
//    template.send("testTopic", "Hello");
//
//    Thread.sleep(30000);
//  }

}

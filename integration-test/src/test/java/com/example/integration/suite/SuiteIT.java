package com.example.integration.suite;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.*;

import com.example.integration.StartupApplication;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

@WireMockTest(httpPort = 28881)
@SpringBootTest(classes = StartupApplication.class)
class SuiteIT {

  @Autowired
  KafkaAdmin kafkaAdmin;

  @Autowired
  KafkaTemplate<String, String> template;

  @BeforeEach
  void setUp() throws Exception {
  }

  @Test
  void test() throws Exception {
    stubFor(post(urlEqualTo("/webhookb2/IncomingWebhook"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("1")));

    template.send("my-topic", """
            {
                "header" : {
                    "id" : "0001",
                    "timestamp" : "2023/05/20"
                },
                "body" : {
                    "text" : "Hello World"
                }
            }
            """);

    await().atMost(5, SECONDS).untilAsserted(
            () -> verify(postRequestedFor(urlEqualTo("/webhookb2/IncomingWebhook"))
                    .withHeader("Content-Type", equalTo("application/json"))
                    .withRequestBody(equalToJson("""
                            {
                              "title": "Notification",
                              "text": "timestamp: 2023/05/20 text: Hello World"
                            }
                            """, true, true))));
  }

}

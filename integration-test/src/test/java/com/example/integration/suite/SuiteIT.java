package com.example.integration.suite;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.*;

import com.example.integration.IntegrationTestConfig;
import com.example.integration.StartupApplication;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.sql.DriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@WireMockTest(httpPort = 28881)
@SpringBootTest(classes = StartupApplication.class)
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, cacheConnection = false)
class SuiteIT {

  private static IntegrationTestConfig conf = new IntegrationTestConfig();

  @Autowired
  KafkaTemplate<String, String> template;

  private static final ConnectionHolder connectionHolder
          = () -> DriverManager.getConnection(conf.getDatabaseUrl(),
                                              conf.getDatabaseUser(),
                                              conf.getDatabasePassword());

  @DynamicPropertySource
  static void setup(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", () -> conf.getDatabaseUrl());
    registry.add("spring.datasource.username", () -> conf.getDatabaseUser());
    registry.add("spring.datasource.password", () -> conf.getDatabasePassword());
  }

  @BeforeEach
  void setUp() throws Exception {
  }

  @Test
  @DataSet(value = "datasets/employee/setup/employees.yml")
  void トピックから取得したメッセージの重要度が有効でIDから名前を取得してメッセージをTeams連携が実行できる場合() throws Exception {
    stubFor(post(urlEqualTo("/webhookb2/IncomingWebhook"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("1")));

    template.send("my-topic", """
            {
                "header" : {
                    "id" : "101",
                    "timestamp" : "2023/05/20",
                    "importance" : true
                },
                "body" : {
                    "text" : "Hello World"
                }
            }
            """);

    await().pollDelay(1, SECONDS).atMost(10, SECONDS).untilAsserted(
            () -> verify(postRequestedFor(urlEqualTo("/webhookb2/IncomingWebhook"))
                    .withHeader("Content-Type", equalTo("application/json"))
                    .withRequestBody(equalToJson("""
                            {
                              "title": "Notification",
                              "text": "timestamp: 2023/05/20 text: Hi, Taro Hello World"
                            }
                            """, true, true))));
  }

}

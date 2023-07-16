package com.example.acceptance.steps;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@CucumberContextConfiguration
@SpringBootTest
public class MyStepdefs {

  @Autowired
  TestContext testContext;

  @Autowired
  KafkaTemplate<String, String> template;

  @前提("TeamsのIncomingWebhookが稼働している")
  public void TeamsのIncomingWebhookが稼働している() {
    WireMockServer wireMockServer = testContext.getWireMockServer();
    wireMockServer.stubFor(post(urlEqualTo("/webhookb2/IncomingWebhook"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("1")));
  }

  @もし("トピックに対象のメッセージが送信される")
  public void トピックに対象のメッセージが送信される() {
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
  }

  @ならば("TeamsのIncomingWebhook経由でチャネルにメッセージが送信される")
  public void TeamsのIncomingWebhook経由でチャネルにメッセージが送信される() {
    WireMockServer wireMockServer = testContext.getWireMockServer();
    await().pollDelay(1, SECONDS).atMost(5, SECONDS).untilAsserted(
            () -> wireMockServer.verify(postRequestedFor(urlEqualTo("/webhookb2/IncomingWebhook"))
                    .withHeader("Content-Type", equalTo("application/json"))
                    .withRequestBody(equalToJson("""
                            {
                              "title": "Notification",
                              "text": "timestamp: 2023/05/20 text: Hi, Taro Hello World"
                            }
                            """, true, true))));
  }

}

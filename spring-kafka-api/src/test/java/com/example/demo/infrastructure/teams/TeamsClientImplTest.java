package com.example.demo.infrastructure.teams;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.example.demo.common.exception.ApplicationException;
import com.example.demo.common.exception.SystemException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.SocketTimeoutException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.ResourceAccessException;

@WireMockTest(httpPort = 28881)
@SpringBootTest
class TeamsClientImplTest {

  @Autowired
  TeamsClientImpl target;

  @Test
  void 正常にメッセージの登録が成功する場合(WireMockRuntimeInfo info) throws Exception {
    // setup
    stubFor(post(urlEqualTo("/webhookb2/IncomingWebhook"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("1")));

    // execute
    target.sendMessage("Test Title", "Test Message");

    // assert
    verify(postRequestedFor(urlEqualTo("/webhookb2/IncomingWebhook"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson("""
                    {
                      "title": "Test Title",
                      "text": "Test Message"
                    }
                    """, true, true)));
  }

  @Test
  void REST通信でタイムアウトが発生した場合(WireMockRuntimeInfo info) throws Exception {
    // setup
    stubFor(post(urlEqualTo("/webhookb2/IncomingWebhook"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withFixedDelay(5000)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("1")));

    // execute & assert
    assertThatThrownBy(() -> target.sendMessage("Test Title", "Test Message"))
            .isInstanceOfSatisfying(
                    SystemException.class,
                    (e) -> {
                      assertThat(e.getCause()).isInstanceOf(ResourceAccessException.class);
                      assertThat(e.getCause().getCause()).isInstanceOf(SocketTimeoutException.class);
                    });

    verify(postRequestedFor(urlEqualTo("/webhookb2/IncomingWebhook"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson("""
                    {
                      "title": "Test Title",
                      "text": "Test Message"
                    }
                    """, true, true)));
  }

  @Test
  void 送信したメッセージが不正である場合(WireMockRuntimeInfo info) throws Exception {
    // setup
    stubFor(post(urlEqualTo("/webhookb2/IncomingWebhook"))
            .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("Summary or Text is required.")));

    // execute & assert
    assertThatThrownBy(() -> target.sendMessage("Test Title", "Test Message"))
            .isInstanceOfSatisfying(
                    ApplicationException.class,
                    (e) -> assertThat(e.getCause().getMessage()).isEqualTo("400 Bad Request: \"Summary or Text is required.\""));

    verify(postRequestedFor(urlEqualTo("/webhookb2/IncomingWebhook"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson("""
                    {
                      "title": "Test Title",
                      "text": "Test Message"
                    }
                    """, true, true)));
  }

  @Test
  void サーバー側で障害が発生した場合(WireMockRuntimeInfo info) throws Exception {
    // setup
    stubFor(post(urlEqualTo("/webhookb2/IncomingWebhook"))
            .willReturn(aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("Server error is occurred.")));

    // execute & assert
    assertThatThrownBy(() -> target.sendMessage("Test Title", "Test Message"))
            .isInstanceOfSatisfying(
                    SystemException.class,
                    (e) -> assertThat(e.getCause().getMessage()).isEqualTo("500 Server Error: \"Server error is occurred.\""));

    verify(postRequestedFor(urlEqualTo("/webhookb2/IncomingWebhook"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalToJson("""
                    {
                      "title": "Test Title",
                      "text": "Test Message"
                    }
                    """, true, true)));
  }

}
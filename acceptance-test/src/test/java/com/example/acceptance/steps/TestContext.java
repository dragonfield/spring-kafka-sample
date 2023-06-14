package com.example.acceptance.steps;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
@ScenarioScope
public class TestContext {

  private WireMockServer wireMockServer = new WireMockServer(options().port(28881));

}

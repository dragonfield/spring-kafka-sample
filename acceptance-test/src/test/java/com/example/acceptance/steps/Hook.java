package com.example.acceptance.steps;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;

public class Hook {

  @Autowired
  TestContext testContext;

  @Before
  public void setUp() {
    testContext.getWireMockServer().start();
  }

  @After
  public void tearDown() {
    testContext.getWireMockServer().stop();
  }

}

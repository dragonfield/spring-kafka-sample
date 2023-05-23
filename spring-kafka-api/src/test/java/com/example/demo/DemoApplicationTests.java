package com.example.demo;

import static org.assertj.core.api.Assertions.*;

import com.example.demo.application.service.NotificationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

  @Autowired
  NotificationUseCase target;

  @Test
  void contextLoads() {
    assertThat(target).isNotNull();
  }

}

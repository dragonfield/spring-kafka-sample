package com.example.demo.application.service;

import static org.mockito.Mockito.*;

import com.example.demo.application.teams.TeamsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class NotificationUseCaseTest {

  @InjectMocks
  NotificationUseCase target;

  @Mock
  TeamsClient teamsClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void 正常にTeams連携が実行できる場合() {
    String timestamp = "2023/05/20";
    String text = "Hello World";

    target.handle(new NotificationCommand(timestamp, text));

    verify(teamsClient)
            .sendMessage("Notification",
                         String.format("timestamp: %s text: %s", timestamp, text));
  }

}
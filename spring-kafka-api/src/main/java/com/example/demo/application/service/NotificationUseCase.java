package com.example.demo.application.service;

import com.example.demo.application.teams.TeamsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationUseCase {

  private final TeamsClient teamsClient;

  public void handle(NotificationCommand command) {
    teamsClient.sendMessage("Notification", "timestamp: " + command.getTimestamp() + " text: " + command.getText());
  }

}

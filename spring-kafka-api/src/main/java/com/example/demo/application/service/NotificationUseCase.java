package com.example.demo.application.service;

import com.example.demo.application.repository.EmployeeRepository;
import com.example.demo.application.teams.TeamsClient;
import com.example.demo.common.Features;
import com.example.demo.domain.model.Employee;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationUseCase {

  private final EmployeeRepository repository;

  private final TeamsClient teamsClient;

  public void handle(NotificationCommand command) {
    if (command.getImportance()) {
      if (Features.NEW_PRODUCT.isActive()) {
        Optional<Employee> employee = repository.findById(command.getId());
        if (employee.isPresent()) {
          teamsClient.sendMessage("Notification", "timestamp: " + command.getTimestamp() + " text: Hi, " + employee.get().getFirstName() + " " + command.getText());
        } else {
          teamsClient.sendMessage("Notification", "timestamp: " + command.getTimestamp() + " text: Hi, everyone " + command.getText());
        }

      } else {
        teamsClient.sendMessage("Notification", "timestamp: " + command.getTimestamp() + " text: " + command.getText());
      }
    }
  }

}

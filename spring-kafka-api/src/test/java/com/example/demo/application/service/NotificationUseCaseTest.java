package com.example.demo.application.service;

import static org.mockito.Mockito.*;

import com.example.demo.application.repository.EmployeeRepository;
import com.example.demo.application.teams.TeamsClient;
import com.example.demo.common.Features;
import com.example.demo.domain.model.Employee;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.togglz.junit5.AllDisabled;
import org.togglz.junit5.AllEnabled;

class NotificationUseCaseTest {

  @InjectMocks
  NotificationUseCase target;

  @Mock
  EmployeeRepository employeeRepository;

  @Mock
  TeamsClient teamsClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @AllDisabled(Features.class)
  void 重要度が有効でTeams連携が実行できる場合() {
    // setup

    // execute
    target.handle(new NotificationCommand("101", "2023/05/20", true, "Hello World"));

    // assert
    verify(teamsClient, times(1))
            .sendMessage("Notification",
                         String.format("timestamp: %s text: %s", "2023/05/20", "Hello World"));
  }

  @Test
  @AllEnabled(Features.class)
  void 重要度が有効でIDから名前を取得してメッセージをTeams連携が実行できる場合() {
    // setup
    when(employeeRepository.findById("101"))
            .thenReturn(Optional.of(new Employee("101", "Taro", "Yamada")));

    // execute
    target.handle(new NotificationCommand("101", "2023/05/20", true, "Hello World"));

    // assert
    verify(teamsClient, times(1))
            .sendMessage("Notification",
                    String.format("timestamp: %s text: %s", "2023/05/20", "Hi, Taro Hello World"));
  }

  @Test
  @AllEnabled(Features.class)
  void 重要度が有効でIDから名前を取得できずメッセージをTeams連携が実行できる場合() {
    // setup
    when(employeeRepository.findById("101"))
            .thenReturn(Optional.empty());

    // execute
    target.handle(new NotificationCommand("101", "2023/05/20", true, "Hello World"));

    // assert
    verify(teamsClient, times(1))
            .sendMessage("Notification",
                    String.format("timestamp: %s text: %s", "2023/05/20", "Hi, everyone Hello World"));
  }

  @Test
  void 重要度が無効でTeams連携が実行できる場合() {
    // setup

    // execute
    target.handle(new NotificationCommand("101", "2023/05/20", false, "Hello World"));

    // assert
    verify(teamsClient, never())
            .sendMessage(any(), any());
  }

}
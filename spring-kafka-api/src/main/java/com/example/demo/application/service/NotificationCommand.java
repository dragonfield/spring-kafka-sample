package com.example.demo.application.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationCommand {

  private String timestamp;

  private String text;

}

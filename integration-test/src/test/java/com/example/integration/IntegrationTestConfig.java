package com.example.integration;

import lombok.Getter;

@Getter
public class IntegrationTestConfig {

  private String databaseUrl;

  private String databaseUser;

  private String databasePassword;

  public IntegrationTestConfig() {
    reload();
  }

  void reload() {
    databaseUrl = System.getProperty("database.url", "jdbc:postgresql://localhost:5432/sample");
    databaseUser = System.getProperty("database.user", "user");
    databasePassword = System.getProperty("database.password", "pass");
  }

}

package com.example.demo.infrastructure.repository.entity;

import com.example.demo.domain.model.Employee;

public record EmployeeEntity(
    String id,
    String firstName,
    String lastName
) {
  public Employee toModel() {
    return new Employee(id, firstName, lastName);
  }
}

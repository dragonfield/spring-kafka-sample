package com.example.demo.application.repository;

import com.example.demo.domain.model.Employee;
import java.util.Optional;

public interface EmployeeRepository {

  Optional<Employee> findById(String id);

}

package com.example.demo.infrastructure.repository;

import static java.util.Objects.nonNull;

import com.example.demo.application.repository.EmployeeRepository;
import com.example.demo.common.exception.SystemException;
import com.example.demo.domain.model.Employee;
import com.example.demo.infrastructure.repository.entity.EmployeeEntity;
import com.example.demo.infrastructure.repository.mapper.EmployeeMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

  private final EmployeeMapper mapper;

  @Override
  public Optional<Employee> findById(String id) {
    try {
      EmployeeEntity entity = mapper.find(id);

      if (nonNull(entity)) {
        return Optional.of(entity.toModel());
      } else {
        return Optional.empty();
      }
    } catch (DataAccessException e) {
      throw new SystemException(e);
    }
  }

}

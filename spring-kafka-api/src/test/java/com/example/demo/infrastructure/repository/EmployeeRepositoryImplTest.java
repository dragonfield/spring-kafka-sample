package com.example.demo.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import com.example.demo.application.repository.EmployeeRepository;
import com.example.demo.domain.model.Employee;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import java.sql.DriverManager;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, cacheConnection = false)
class EmployeeRepositoryImplTest {

  @Container
  private static final PostgreSQLContainer<?> postgresqlContainer
          = new PostgreSQLContainer<>(DockerImageName.parse(PostgreSQLContainer.IMAGE).withTag("15.3"))
                  .withUsername("user")
                  .withPassword("pass")
                  .withDatabaseName("sample")
                  .withInitScript("schema.sql");

  private static final ConnectionHolder connectionHolder
          = () -> DriverManager.getConnection(postgresqlContainer.getJdbcUrl(),
                                              postgresqlContainer.getUsername(),
                                              postgresqlContainer.getPassword());

  @DynamicPropertySource
  static void setup(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresqlContainer::getUsername);
    registry.add("spring.datasource.password", postgresqlContainer::getPassword);
  }

  @Autowired
  EmployeeRepository target;

  @Test
  @DataSet(value = "datasets/employee/setup/employees.yml")
  void 正常に指定したIDで従業員が取得できる場合() {
    // setup

    // execute
    Optional<Employee> actual = target.findById("101");

    // assert
    Optional<Employee> expected = Optional.of(new Employee("101", "Taro", "Yamada"));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DataSet(value = "datasets/employee/setup/employees.yml")
  void 指定したIDで従業員が取得できない場合() {
    // setup

    // execute
    Optional<Employee> actual = target.findById("9999");

    // assert
    Optional<Employee> expected = Optional.empty();
    assertThat(actual).isEqualTo(expected);
  }

}
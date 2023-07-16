package com.example.demo.infrastructure.repository.mapper;

import static org.assertj.core.api.Assertions.*;

import com.example.demo.infrastructure.repository.entity.EmployeeEntity;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import java.sql.DriverManager;
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
class EmployeeMapperTest {

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
  EmployeeMapper target;

  @Test
  @DataSet(value = "datasets/employee/setup/employees.yml")
  void 指定したIDでエンティティが取得できる場合() {
    // setup

    // execute
    EmployeeEntity actual = target.find("101");

    // assert
    EmployeeEntity expected = new EmployeeEntity("101", "Taro", "Yamada");
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DataSet(value = "datasets/employee/setup/employees.yml")
  void 指定したIDでエンティティが取得できない場合() {
    // setup

    // execute
    EmployeeEntity actual = target.find("9999");

    // assert
    assertThat(actual).isNull();
  }

}
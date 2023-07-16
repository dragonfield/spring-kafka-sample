package com.example.acceptance.steps;

import com.example.acceptance.AcceptanceTestConfig;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DBUnitConfig;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import java.sql.DriverManager;
import org.springframework.beans.factory.annotation.Autowired;

public class Hook {

  private static AcceptanceTestConfig conf = new AcceptanceTestConfig();

  private static final ConnectionHolder connectionHolder
          = () -> DriverManager.getConnection(conf.getDatabaseUrl(),
                                              conf.getDatabaseUser(),
                                              conf.getDatabasePassword());


  @Autowired
  TestContext testContext;

  @Before
  public void setUp() throws Exception {
    testContext.getWireMockServer().start();

    DataSetExecutor dbunitExecutor = DataSetExecutorImpl.instance(connectionHolder);
    dbunitExecutor.setDBUnitConfig(new DBUnitConfig().addDBUnitProperty("caseSensitiveTableNames", true));
    dbunitExecutor.createDataSet(new DataSetConfig("datasets/employee/setup/employees.yml"));
  }

  @After
  public void tearDown() {
    testContext.getWireMockServer().stop();
  }

}

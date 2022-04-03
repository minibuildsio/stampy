package co.uk.howardpaget.stampy.util;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {
  private static final String IMAGE_VERSION = "postgres:11.1";
  private static PostgresContainer container;

  private PostgresContainer() {
    super(IMAGE_VERSION);
  }

  public static PostgresContainer getInstance() {
    if (container == null) {
      container = new PostgresContainer().withDatabaseName("stampydb");
      container.start();
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("DB_URL", container.getJdbcUrl());
    System.setProperty("DB_USERNAME", container.getUsername());
    System.setProperty("DB_PASSWORD", container.getPassword());
  }

}

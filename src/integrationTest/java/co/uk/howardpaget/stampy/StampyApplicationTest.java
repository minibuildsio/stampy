package co.uk.howardpaget.stampy;


import com.jayway.jsonpath.JsonPath;
import org.assertj.db.type.Request;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.assertj.db.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class StampyApplicationTest {
  private static final String POSTGRES_IMAGE = "postgres:11.1";

  @Container
  private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(POSTGRES_IMAGE).withDatabaseName("stampydb");

  @Autowired
  MockMvc mockMvc;

  @Autowired
  DataSource dataSource;

  @BeforeAll
  static void beforeAll() {
    System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
    System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
    System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
  }

  @Test
  void stamp_table_is_created_on_start_up() {
    Table table = new Table(dataSource, "stamp");

    assertThat(table)
        .exists()
        .column("id").isNumber(false)
        .column("name").isText(false);
  }

  @Test
  void create_stamp_writes_to_the_stamp_table() throws Exception {

    // When: a stamp is created using POST /stamps
    mockMvc.perform(post("/stamps").contentType(APPLICATION_JSON).content("{\"name\": \"DC Collection - Alfred\"}"));

    // Then: the stamp table will contain the new stamp
    Table table = new Table(dataSource, "stamp");

    assertThat(table)
        .hasNumberOfRows(1)
        .row(0)
        .value("id").isEqualTo(1)
        .value("name").isEqualTo("DC Collection - Alfred");
  }

  @Test
  void delete_stamp_removes_stamp_from_the_stamp_table() throws Exception {

    // Given: an existing stamp
    MvcResult result = mockMvc.perform(post("/stamps").contentType(APPLICATION_JSON).content("{\"name\": \"DC Collection - Batwomen\"}")).andReturn();

    int id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

    // When: the stamp is deleted using DELETE /stamps/{id}
    mockMvc.perform(delete("/stamps/" + id));

    // Then: the stamp table will not contain the stamp
    Request request = new Request(dataSource,"select id from stamp where id = ?;", id);

    assertThat(request).isEmpty();
  }

}

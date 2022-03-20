package co.uk.howardpaget.stampy.controller;

import co.uk.howardpaget.stampy.entity.StampEntity;
import co.uk.howardpaget.stampy.repository.StampRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StampControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  StampRepository stampRepository;

  @Test
  public void get_stamp_returns_stamps_from_stamp_repository_as_json() throws Exception {
    // Given: repository returns stamps
    when(stampRepository.findAll()).thenReturn(Arrays.asList(new StampEntity("Halfpenny Rose Red"), new StampEntity("One Shilling embossed")));

    // When: get stamps is called
    var result = mockMvc.perform(get("/stamps"));

    // Then: the stamps are returned in json format
    result
        .andExpect(status().isOk())
        .andExpect(content().json("[{'name': 'Halfpenny Rose Red'}, {'name': 'One Shilling embossed'}]"));

    // And: the stamp repository is called
    verify(stampRepository, times(1)).findAll();
  }


  @Test
  public void post_stamp_saves_stamp_to_stamp_repository() throws Exception {
    // When: get stamps is called
    var result = mockMvc.perform(post("/stamps").contentType(APPLICATION_JSON).content("{\"name\": \"DC Collection - Alfred\"}"));

    // Then: the stamps are returned in json format
    result.andExpect(status().isOk());

    // And: the stamp repository is called
    verify(stampRepository, times(1)).save(argThat(a -> a.getName().equals("DC Collection - Alfred")));
  }

  @Test
  public void delete_existent_stamp_returns_success() throws Exception {
    // Given: a stamp with id 10 exists
    when(stampRepository.existsById(eq(10L))).thenReturn(true);

    // When: delete stamps with id = 10 is called
    var result = mockMvc.perform(delete("/stamps/10"));

    // Then: success is returned
    result.andExpect(status().isOk());

    // And: the stamp repository delete by id is called passing 10
    verify(stampRepository, times(1)).deleteById(eq(10L));
  }

  @Test
  public void delete_non_existent_stamp_returns_not_found() throws Exception {
    // Given: a stamp with id 10 exists
    when(stampRepository.existsById(eq(10L))).thenReturn(false);

    // When: delete stamps with id = 10 is called
    var result = mockMvc.perform(delete("/stamps/10"));

    // Then: success is returned
    result.andExpect(status().isNotFound());

    // And: the stamp repository delete by id is called passing 10
    verify(stampRepository, never()).deleteById(any());
  }

}
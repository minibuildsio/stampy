package co.uk.howardpaget.stampy.controller;

import co.uk.howardpaget.stampy.controller.dto.AuthRequest;
import co.uk.howardpaget.stampy.controller.dto.AuthResponse;
import co.uk.howardpaget.stampy.entity.StampyUserEntity;
import co.uk.howardpaget.stampy.repository.UserRepository;
import co.uk.howardpaget.stampy.util.JwtHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtHelper jwtHelper;

  private final BCryptPasswordEncoder bCryptPasswordEncoder = new  BCryptPasswordEncoder();

  @Captor
  private ArgumentCaptor<StampyUserEntity> userCaptor;

  private AuthController authController;

  @BeforeEach
  void setUp() {
    authController = new AuthController(userRepository, jwtHelper, bCryptPasswordEncoder);
  }

  @Test
  void when_signing_up_with_unused_username_user_details_written_to_db_and_a_token_is_returned() {
    when(jwtHelper.createJwt(any(), any())).thenReturn("a-token");

    ResponseEntity<AuthResponse> response = authController.signup(new AuthRequest("minibuilds", "ducky"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    verify(userRepository, times(1)).existsByUsername("minibuilds");
    verify(userRepository, times(1)).save(userCaptor.capture());

    assertThat(userCaptor.getValue().getUsername()).isEqualTo("minibuilds");
    assertThat(userCaptor.getValue().getPasswordHash()).matches(p -> bCryptPasswordEncoder.matches("ducky", p));

    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void when_signing_up_with_an_in_use_username_unauthorised_is_returned() {
    when(userRepository.existsByUsername("minibuilds")).thenReturn(true);

    ResponseEntity<AuthResponse> response = authController.signup(new AuthRequest("minibuilds", "ducky"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    verify(userRepository, times(1)).existsByUsername("minibuilds");
    verify(userRepository, times(0)).save(userCaptor.capture());
  }


  @Test
  void when_logging_in_with_incorrect_login_details_unauthorised_is_returned() {
    ResponseEntity<AuthResponse> response = authController.login(new AuthRequest("minibuilds", "ducky"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    verify(userRepository, times(1)).findUser("minibuilds", "ducky");
  }

  @Test
  void when_logging_in_with_correct_login_details_a_token_is_returned() {
    when(jwtHelper.createJwt(any(), any())).thenReturn("a-token");

    when(userRepository.findUser("minibuilds", "ducky"))
        .thenReturn(Optional.of(new StampyUserEntity(1L, "minibuilds", "password_hash")));

    ResponseEntity<AuthResponse> response = authController.login(new AuthRequest("minibuilds", "ducky"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    verify(userRepository, times(1)).findUser("minibuilds", "ducky");

    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().token()).isEqualTo("a-token");
  }
}
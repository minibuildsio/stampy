package co.uk.howardpaget.stampy.util;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class JwtHelperTest {

  private static final String EXPIRED_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDciLCJyb2xlcyI6WyJMaWNlbmNlIHRvIGtpbGwiXSwiZXhwIjoxNjc5NzI2MTEwfQ.DoNb7sxJ_3umTh0Ff864MUpvkTXBK_BquMVngAv5B9x0GPOvmhDyJQhusDZIED7sfYgAQwjA9V4IPWJkTKsqSQ";

  private final JwtHelper jwtHelper = new JwtHelper("a-secret", 86400);
  private final JwtHelper jwtHelperDifferentSecret = new JwtHelper("a-different-secret", 86400);


  @Test
  void create_jwt_create_token_with_user_id_and_roles() {
    String jwt = jwtHelper.createJwt("007", List.of("Licence to kill"));

    User user = jwtHelper.extractUser(jwt);

    assertThat(user.getUsername()).isEqualTo("007");
    assertThat(user.getAuthorities().stream().map(GrantedAuthority::getAuthority)).contains("Licence to kill");
  }

  @Test
  void an_expired_token_throws_an_exception() {
    assertThatThrownBy(() -> jwtHelper.extractUser(EXPIRED_JWT))
        .isInstanceOf(TokenExpiredException.class);
  }

  @Test
  void token_verification_fails_if_token_was_signed_with_a_different_secret() {
    String jwt = jwtHelperDifferentSecret.createJwt("007", List.of("Licence to kill"));

    assertThatThrownBy(() -> jwtHelper.extractUser(jwt))
        .isInstanceOf(SignatureVerificationException.class);
  }

}
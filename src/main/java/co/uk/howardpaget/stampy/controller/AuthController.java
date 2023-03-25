package co.uk.howardpaget.stampy.controller;

import co.uk.howardpaget.stampy.controller.dto.AuthRequest;
import co.uk.howardpaget.stampy.controller.dto.AuthResponse;
import co.uk.howardpaget.stampy.entity.StampyUserEntity;
import co.uk.howardpaget.stampy.repository.UserRepository;
import co.uk.howardpaget.stampy.util.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthController {

  private final UserRepository userRepository;
  private final JwtHelper jwtHelper;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public AuthController(UserRepository userRepository, JwtHelper jwtHelper, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.jwtHelper = jwtHelper;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
    Optional<StampyUserEntity> userOptional = userRepository.findUser(authRequest.username(), authRequest.password());

    if (userOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(new AuthResponse(jwtHelper.createJwt(authRequest.username(), List.of())));
  }

  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signup(@RequestBody AuthRequest authRequest) {
    StampyUserEntity user = new StampyUserEntity(null, authRequest.username().trim(), bCryptPasswordEncoder.encode(authRequest.password()));

    if (userRepository.existsByUsername(user.getUsername())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    userRepository.save(user);

    return ResponseEntity.ok(new AuthResponse(jwtHelper.createJwt(authRequest.username(), List.of())));
  }
}

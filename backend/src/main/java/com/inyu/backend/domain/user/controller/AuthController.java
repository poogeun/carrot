package com.inyu.backend.domain.user.controller;

import com.inyu.backend.domain.user.dto.LoginRequest;
import com.inyu.backend.domain.user.dto.ReissueRequest;
import com.inyu.backend.domain.user.dto.SignupRequest;
import com.inyu.backend.domain.user.dto.TokenResponse;
import com.inyu.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Long> signup(@Valid @RequestBody SignupRequest request) {
    Long userId = userService.signup(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(userId);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
    TokenResponse tokenResponse = userService.login(request);
    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/reissue")
  public ResponseEntity<TokenResponse> reissue(@Valid @RequestBody ReissueRequest request) {
    TokenResponse tokenResponse = userService.reissue(request.getRefreshToken());
    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(Authentication authentication) {
    userService.logout(authentication.getName());
    return ResponseEntity.noContent().build();
  }
}

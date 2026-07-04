package com.inyu.backend.domain.user.service;

import com.inyu.backend.domain.user.dto.LoginRequest;
import com.inyu.backend.domain.user.dto.SignupRequest;
import com.inyu.backend.domain.user.dto.TokenResponse;
import com.inyu.backend.domain.user.entity.User;
import com.inyu.backend.domain.user.repository.UserRepository;
import com.inyu.backend.global.security.JwtProvider;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final StringRedisTemplate redisTemplate;

  public Long signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user = User.builder()
        .email(request.getEmail())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .build();

    return userRepository.save(user).getId();
  }

  public TokenResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole());
    String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

    redisTemplate.opsForValue().set(
        "RT:" + user.getEmail(),
        refreshToken,
        Duration.ofMillis(jwtProvider.getRefreshExpiration())
    );

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public TokenResponse reissue(String refreshToken) {
    if (!jwtProvider.validateToken(refreshToken)) {
      throw new IllegalArgumentException("유효하지 않은 refresh 토큰입니다.");
    }

    String email = jwtProvider.getEmail(refreshToken);
    String savedToken = redisTemplate.opsForValue().get("RT:" + email);

    if (savedToken == null || !savedToken.equals(refreshToken)) {
      throw new IllegalArgumentException("일치하지 않는 refresh 토큰입니다.");
    }

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    String newAccessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole());

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public void logout(String email) {
    redisTemplate.delete("RT:" + email);
  }
}

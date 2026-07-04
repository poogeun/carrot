package com.inyu.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
}

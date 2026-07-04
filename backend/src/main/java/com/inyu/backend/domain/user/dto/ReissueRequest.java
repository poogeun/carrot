package com.inyu.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequest {

  @NotBlank
  private String refreshToken;
}

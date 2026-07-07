package com.inyu.backend.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest(
    @NotNull Long chatRoomId,
    @NotBlank String content
) {

}

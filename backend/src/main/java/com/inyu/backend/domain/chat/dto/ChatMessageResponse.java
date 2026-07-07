package com.inyu.backend.domain.chat.dto;

import com.inyu.backend.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;

public record ChatMessageResponse(
    Long id,
    Long senderId,
    String senderNickname,
    String content,
    LocalDateTime createdAt
) {
  public static ChatMessageResponse from(ChatMessage chatMessage) {
    return new ChatMessageResponse(
        chatMessage.getId(),
        chatMessage.getSender().getId(),
        chatMessage.getSender().getNickname(),
        chatMessage.getContent(),
        chatMessage.getCreatedAt()
    );
  }
}

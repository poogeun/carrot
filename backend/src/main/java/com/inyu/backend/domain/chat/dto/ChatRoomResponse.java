package com.inyu.backend.domain.chat.dto;

import com.inyu.backend.domain.chat.entity.ChatRoom;
import java.time.LocalDateTime;

public record ChatRoomResponse(
    Long id,

    Long productId,
    String productTitle,

    Long sellerId,
    String sellerNickname,
    Long buyerId,
    String buyerNickname,

    LocalDateTime createdAt
) {
  public static ChatRoomResponse from(ChatRoom chatRoom) {
    return new ChatRoomResponse(
        chatRoom.getId(),
        chatRoom.getProduct().getId(),
        chatRoom.getProduct().getTitle(),
        chatRoom.getProduct().getSeller().getId(),
        chatRoom.getProduct().getSeller().getNickname(),
        chatRoom.getBuyer().getId(),
        chatRoom.getBuyer().getNickname(),
        chatRoom.getCreatedAt()
    );
  }
}

package com.inyu.backend.domain.chat.service;

import com.inyu.backend.domain.chat.dto.ChatMessageResponse;
import com.inyu.backend.domain.chat.entity.ChatMessage;
import com.inyu.backend.domain.chat.entity.ChatRoom;
import com.inyu.backend.domain.chat.repository.ChatMessageRepository;
import com.inyu.backend.domain.chat.repository.ChatRoomRepository;
import com.inyu.backend.domain.user.entity.User;
import com.inyu.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;

  public ChatMessageResponse sendMessage(Long chatRoomId, String senderEmail, String content) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

    User sender = userRepository.findByEmail(senderEmail)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    Long senderId = sender.getId();
    if (!senderId.equals(chatRoom.getBuyer().getId())
        && !senderId.equals(chatRoom.getProduct().getSeller().getId())) {
      throw new AccessDeniedException("채팅방 접근 권한이 없습니다.");
    }

    ChatMessage chatMessage = ChatMessage.builder()
        .chatRoom(chatRoom)
        .content(content)
        .sender(sender)
        .build();

    chatMessageRepository.save(chatMessage);

    return ChatMessageResponse.from(chatMessage);
  }

}

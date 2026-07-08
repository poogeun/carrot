package com.inyu.backend.domain.chat.controller;

import com.inyu.backend.domain.chat.dto.ChatMessageResponse;
import com.inyu.backend.domain.chat.dto.ChatRoomResponse;
import com.inyu.backend.domain.chat.service.ChatMessageService;
import com.inyu.backend.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final ChatMessageService chatMessageService;

  @PostMapping("/products/{productId}/chat-rooms")
  public ResponseEntity<ChatRoomResponse> createChatRoom(
      @PathVariable Long productId,
      Authentication authentication
  ) {
    String email = authentication.getName();
    return ResponseEntity.ok(chatRoomService.createChatRoom(email, productId));
  }

  @GetMapping("/chat-rooms")
  public ResponseEntity<Page<ChatRoomResponse>> getMyChatRooms(
      Authentication authentication,
      Pageable pageable
  ) {
    String email = authentication.getName();
    return ResponseEntity.ok(chatRoomService.getMyChatRooms(email, pageable));
  }

  @GetMapping("/chat-rooms/{roomId}/messages")
  public ResponseEntity<Page<ChatMessageResponse>> getMessages(
      @PathVariable Long roomId,
      Authentication authentication,
      Pageable pageable
  ) {
    String email = authentication.getName();
    return ResponseEntity.ok(chatMessageService.getMessages(roomId, email, pageable));
  }
}

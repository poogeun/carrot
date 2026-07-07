package com.inyu.backend.domain.chat.controller;

import com.inyu.backend.domain.chat.dto.ChatMessageRequest;
import com.inyu.backend.domain.chat.dto.ChatMessageResponse;
import com.inyu.backend.domain.chat.service.ChatMessageService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

  private final ChatMessageService chatMessageService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/chat/message")
  public void sendMessage(@Valid ChatMessageRequest request, Principal principal) {
    String senderEmail = principal.getName();

    ChatMessageResponse response =
        chatMessageService.sendMessage(request.chatRoomId(), senderEmail, request.content());

    simpMessagingTemplate.convertAndSend(
        "/topic/chat/room/" + request.chatRoomId(), response
    );
  }

}

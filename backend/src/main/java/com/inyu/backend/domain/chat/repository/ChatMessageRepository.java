package com.inyu.backend.domain.chat.repository;

import com.inyu.backend.domain.chat.entity.ChatMessage;
import com.inyu.backend.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  Page<ChatMessage> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom, Pageable pageable);

}

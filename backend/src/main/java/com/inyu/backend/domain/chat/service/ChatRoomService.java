package com.inyu.backend.domain.chat.service;

import com.inyu.backend.domain.chat.dto.ChatRoomResponse;
import com.inyu.backend.domain.chat.entity.ChatRoom;
import com.inyu.backend.domain.chat.repository.ChatRoomRepository;
import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.product.repository.ProductRepository;
import com.inyu.backend.domain.user.entity.User;
import com.inyu.backend.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Transactional
  public ChatRoomResponse createChatRoom(String buyerEmail, Long productId) {
    User buyer = userRepository.findByEmail(buyerEmail)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

    if (product.getSeller().getId().equals(buyer.getId())) {
      throw new IllegalArgumentException("본인 상품에는 채팅방을 만들 수 없습니다.");
    }

    Optional<ChatRoom> existing = chatRoomRepository.findByProductAndBuyer(product, buyer);
    if (existing.isPresent()) {
      return ChatRoomResponse.from(existing.get());
    }

    ChatRoom newRoom = ChatRoom.builder()
            .product(product)
            .buyer(buyer)
            .build();
    chatRoomRepository.save(newRoom);
    return ChatRoomResponse.from(newRoom);
  }

  @Transactional(readOnly = true)
  public Page<ChatRoomResponse> getMyChatRooms(String email, Pageable pageable) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    return chatRoomRepository.findByBuyerOrProductSeller(user, pageable)
        .map(ChatRoomResponse::from);
  }

}

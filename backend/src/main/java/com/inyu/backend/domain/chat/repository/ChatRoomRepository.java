package com.inyu.backend.domain.chat.repository;

import com.inyu.backend.domain.chat.entity.ChatRoom;
import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findByProductAndBuyer(Product product, User buyer);

}

package com.inyu.backend.domain.chat.repository;

import com.inyu.backend.domain.chat.entity.ChatRoom;
import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findByProductAndBuyer(Product product, User buyer);

  @Query("SELECT cr FROM ChatRoom cr WHERE cr.buyer = :user OR cr.product.seller = :user")
  Page<ChatRoom> findByBuyerOrProductSeller(@Param("user") User user, Pageable pageable);

}

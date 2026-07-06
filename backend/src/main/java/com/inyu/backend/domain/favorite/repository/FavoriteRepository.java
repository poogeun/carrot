package com.inyu.backend.domain.favorite.repository;

import com.inyu.backend.domain.favorite.entity.Favorite;
import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  Optional<Favorite> findByUserAndProduct(User user, Product product);

  Page<Favorite> findByUser(User user, Pageable pageable);

}

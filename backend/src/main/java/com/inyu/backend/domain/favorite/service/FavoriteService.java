package com.inyu.backend.domain.favorite.service;

import com.inyu.backend.domain.favorite.dto.FavoriteToggleResponse;
import com.inyu.backend.domain.favorite.entity.Favorite;
import com.inyu.backend.domain.favorite.repository.FavoriteRepository;
import com.inyu.backend.domain.product.dto.ProductResponse;
import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.product.repository.ProductRepository;
import com.inyu.backend.domain.product.service.ProductService;
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
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final ProductService productService;

  @Transactional
  public FavoriteToggleResponse toggleFavorite(String email, Long productId) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

    Optional<Favorite> existing = favoriteRepository.findByUserAndProduct(user, product);

    if (existing.isPresent()) {
      favoriteRepository.delete(existing.get());
      return new FavoriteToggleResponse(false);
    } else {
      favoriteRepository.save(Favorite.builder().user(user).product(product).build());
      return new FavoriteToggleResponse(true);
    }
  }
  
  @Transactional(readOnly = true)
  public Page<ProductResponse> getMyFavorites(String email, Pageable pageable) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    
    return favoriteRepository.findByUser(user, pageable)
        .map(favorite -> productService.toProductResponse(favorite.getProduct()));
  }

}

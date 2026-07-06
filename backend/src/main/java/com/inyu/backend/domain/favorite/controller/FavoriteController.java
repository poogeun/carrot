package com.inyu.backend.domain.favorite.controller;

import com.inyu.backend.domain.favorite.dto.FavoriteToggleResponse;
import com.inyu.backend.domain.favorite.service.FavoriteService;
import com.inyu.backend.domain.product.dto.ProductResponse;
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
public class FavoriteController {

  private final FavoriteService favoriteService;

  @PostMapping("/products/{productId}/favorites")
  public ResponseEntity<FavoriteToggleResponse> toggleFavorite(
      @PathVariable Long productId,
      Authentication authentication
  ) {
    String email = authentication.getName();
    return ResponseEntity.ok(favoriteService.toggleFavorite(email, productId));
  }

  @GetMapping("/favorites")
  public ResponseEntity<Page<ProductResponse>> getMyFavorites(
      Authentication authentication,
      Pageable pageable
  ) {
    String email = authentication.getName();
    return ResponseEntity.ok(favoriteService.getMyFavorites(email, pageable));
  }

}

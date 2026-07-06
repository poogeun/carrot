package com.inyu.backend.domain.product.dto;

import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.product.entity.ProductCategory;
import com.inyu.backend.domain.product.entity.ProductImage;
import com.inyu.backend.domain.product.entity.ProductStatus;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record ProductResponse(
    Long id,
    Long sellerId,
    String sellerNickname,
    String title,
    String description,
    Integer price,
    ProductCategory category,
    ProductStatus status,
    List<String> imageUrls,
    LocalDateTime createdAt,
    Long viewCount
) {

  public static ProductResponse from(Product product, Long viewCount) {
    List<String> imageUrls = product.getImages().stream()
        .sorted(Comparator.comparing(ProductImage::getSortOrder))
        .map(ProductImage::getImageUrl)
        .toList();

    return new ProductResponse(
        product.getId(),
        product.getSeller().getId(),
        product.getSeller().getNickname(),
        product.getTitle(),
        product.getDescription(),
        product.getPrice(),
        product.getCategory(),
        product.getStatus(),
        imageUrls,
        product.getCreatedAt(),
        viewCount
    );
  }
}

package com.inyu.backend.domain.product.service;

import com.inyu.backend.domain.product.dto.ProductCreateRequest;
import com.inyu.backend.domain.product.dto.ProductResponse;
import com.inyu.backend.domain.product.dto.ProductUpdateRequest;
import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.product.entity.ProductCategory;
import com.inyu.backend.domain.product.entity.ProductImage;
import com.inyu.backend.domain.product.repository.ProductRepository;
import com.inyu.backend.domain.user.entity.User;
import com.inyu.backend.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final StringRedisTemplate redisTemplate;

  public ProductResponse createProduct(String email, ProductCreateRequest request) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    Product product = Product.builder()
        .seller(user)
        .title(request.title())
        .description(request.description())
        .price(request.price())
        .category(request.category())
        .build();

    List<String> imageUrls = request.imageUrls();
    for (int i=0; i < imageUrls.size(); i++) {
      ProductImage image = ProductImage.builder()
          .imageUrl(imageUrls.get(i))
          .sortOrder(i)
          .build();
      product.addImage(image);
    }

    productRepository.save(product);
    return ProductResponse.from(product, 0L);
  }

  @Transactional(readOnly = true)
  public ProductResponse getProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

    Long viewCount = redisTemplate.opsForValue().increment("product:view:" + id);

    return ProductResponse.from(product, viewCount);
  }

  @Transactional(readOnly = true)
  public Page<ProductResponse> getProducts(ProductCategory category, Pageable pageable) {
    Page<Product> products = category == null
        ? productRepository.findAll(pageable)
        : productRepository.findByCategory(category, pageable);

    return products.map(product -> ProductResponse.from(product, getViewCount(product.getId())));
  }

  public ProductResponse updateProduct(String email, Long id, ProductUpdateRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

    if (!product.getSeller().getEmail().equals(email)) {
      throw new AccessDeniedException("본인이 등록한 상품만 수정할 수 있습니다.");
    }

    product.updateInfo(
        request.title(),
        request.description(),
        request.price(),
        request.category()
    );

    Long viewCount = getViewCount(id);

    return ProductResponse.from(product, viewCount);
  }

  public void deleteProduct(String email, Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

    if (!product.getSeller().getEmail().equals(email)) {
      throw new AccessDeniedException("본인이 등록한 상품만 삭제할 수 있습니다.");
    }

    productRepository.delete(product);
  }

  private Long getViewCount(Long id) {
    String value = redisTemplate.opsForValue().get("product:view:" + id);
    return value != null ? Long.parseLong(value) : 0L;
  }

  @Transactional(readOnly = true)
  public ProductResponse toProductResponse(Product product) {
    return ProductResponse.from(product, getViewCount(product.getId()));
  }

}

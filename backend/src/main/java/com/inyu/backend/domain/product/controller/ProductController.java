package com.inyu.backend.domain.product.controller;

import com.inyu.backend.domain.product.dto.ProductCreateRequest;
import com.inyu.backend.domain.product.dto.ProductResponse;
import com.inyu.backend.domain.product.dto.ProductUpdateRequest;
import com.inyu.backend.domain.product.entity.ProductCategory;
import com.inyu.backend.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(
      Authentication authentication,
      @Valid @RequestBody ProductCreateRequest request
  ) {
    String email = authentication.getName();
    ProductResponse response = productService.createProduct(email, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProduct(id));
  }

  @GetMapping
  public ResponseEntity<Page<ProductResponse>> getProducts(
      @RequestParam(required = false) ProductCategory category,
      Pageable pageable
  ) {
    return ResponseEntity.ok(productService.getProducts(category, pageable));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProductResponse> updateProduct(
      Authentication authentication,
      @PathVariable Long id,
      @Valid @RequestBody ProductUpdateRequest request
  ) {
    String email = authentication.getName();
    ProductResponse response = productService.updateProduct(email, id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(
      Authentication authentication,
      @PathVariable Long id
  ) {
    String email = authentication.getName();
    productService.deleteProduct(email, id);
    return ResponseEntity.noContent().build();
  }
}

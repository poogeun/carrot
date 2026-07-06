package com.inyu.backend.domain.product.repository;

import com.inyu.backend.domain.product.entity.Product;
import com.inyu.backend.domain.product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findByCategory(ProductCategory category, Pageable pageable);
}

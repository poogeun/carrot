package com.inyu.backend.domain.product.dto;

import com.inyu.backend.domain.product.entity.ProductCategory;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
    @Size(min = 1) String title,
    @Size(min = 1) String description,
    @Positive Integer price,
    ProductCategory category
) {

}

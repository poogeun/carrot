package com.inyu.backend.domain.product.dto;

import com.inyu.backend.domain.product.entity.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ProductCreateRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull @Positive Integer price,
    @NotNull ProductCategory category,
    @NotNull @Size(min = 1) List<String> imageUrls
    ) {

}

package com.inyu.backend.domain.image.dto;

import jakarta.validation.constraints.NotBlank;

public record ImagePresignedUrlRequest(
    @NotBlank String filename
) {

}

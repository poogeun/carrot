package com.inyu.backend.domain.image.dto;

public record ImagePresignedUrlResponse(
    String uploadUrl,
    String imageUrl
) {

}

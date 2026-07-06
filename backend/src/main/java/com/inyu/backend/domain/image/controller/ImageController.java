package com.inyu.backend.domain.image.controller;

import com.inyu.backend.domain.image.dto.ImagePresignedUrlRequest;
import com.inyu.backend.domain.image.dto.ImagePresignedUrlResponse;
import com.inyu.backend.domain.image.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping("/presigned-url")
  public ResponseEntity<ImagePresignedUrlResponse> createPresignedUrl(
      @Valid @RequestBody ImagePresignedUrlRequest request
  ) {
    ImagePresignedUrlResponse response = imageService.createPresignedUrl(request.filename());
    return ResponseEntity.ok(response);
  }

}

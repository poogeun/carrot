package com.inyu.backend.domain.image.service;

import com.inyu.backend.domain.image.dto.ImagePresignedUrlResponse;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class ImageService {

  private final S3Presigner s3Presigner;
  private final String bucket;

  public ImageService(
      S3Presigner s3Presigner,
      @Value("${aws.s3.bucket}") String bucket
  ) {
    this.s3Presigner = s3Presigner;
    this.bucket = bucket;
  }

  public ImagePresignedUrlResponse createPresignedUrl(String filename) {
    String key = "products/" + UUID.randomUUID() + "-" + filename;

    // 어느 버킷, 어느 키에 업로드 할건지
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    // 유효 시간 지정
    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .putObjectRequest(putObjectRequest)
        .build();

    // 실제 서명받고 url 반환
    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

    URL rawUrl = presignedRequest.url();
    String uploadUrl = rawUrl.toString();
    String imageUrl = rawUrl.getProtocol() + "://" + rawUrl.getHost() + rawUrl.getPath();

    return new ImagePresignedUrlResponse(uploadUrl, imageUrl);
  }

}

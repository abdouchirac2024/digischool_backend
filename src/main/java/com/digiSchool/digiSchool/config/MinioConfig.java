package com.digiSchool.digiSchool.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

  private static final Logger log = LoggerFactory.getLogger(MinioConfig.class);

  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.access-key}")
  private String accessKey;

  @Value("${minio.secret-key}")
  private String secretKey;

  @Value("${minio.bucket.avatars:avatars}")
  private String avatarBucket;

  @Value("${minio.bucket.documents:documents}")
  private String documentBucket;

  @Bean
  public MinioClient minioClient() {
    MinioClient client = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secretKey)
        .build();

    // Bucket avatars : lecture publique (URLs directes dans le frontend)
    ensureBucketExists(client, avatarBucket, true);
    // Bucket documents : privé (accès via URL pré-signées uniquement)
    ensureBucketExists(client, documentBucket, false);

    return client;
  }

  private void ensureBucketExists(MinioClient client, String bucket, boolean publicRead) {
    try {
      boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
      if (!exists) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        log.info("Bucket MinIO '{}' créé (publicRead={})", bucket, publicRead);
      }
      if (publicRead) {
        applyPublicReadPolicy(client, bucket);
      }
    } catch (Exception e) {
      log.warn("Impossible de vérifier/créer le bucket MinIO '{}': {}", bucket, e.getMessage());
    }
  }

  private void applyPublicReadPolicy(MinioClient client, String bucket) {
    String policy = """
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Effect": "Allow",
              "Principal": {"AWS": ["*"]},
              "Action": ["s3:GetObject"],
              "Resource": ["arn:aws:s3:::%s/*"]
            }
          ]
        }
        """.formatted(bucket);
    try {
      client.setBucketPolicy(SetBucketPolicyArgs.builder()
          .bucket(bucket)
          .config(policy)
          .build());
      log.info("Politique lecture publique appliquée sur bucket '{}'", bucket);
    } catch (Exception e) {
      log.warn("Impossible d'appliquer la politique publique sur '{}': {}", bucket, e.getMessage());
    }
  }
}
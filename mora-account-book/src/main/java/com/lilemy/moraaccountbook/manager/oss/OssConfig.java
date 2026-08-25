package com.lilemy.moraaccountbook.manager.oss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * 对象存储配置类
 *
 * @author lilemy
 * @date 2026-08-25
 */
@Slf4j
@Configuration
public class OssConfig {

    /**
     * 创建 S3 客户端
     *
     * @param properties RustFS 配置
     * @return S3 客户端
     */
    @Bean
    public S3Client s3Client(OssProperties properties) {
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())
                        )
                )
                // RustFS 必须开启
                .forcePathStyle(true)
                .build();
        String bucket = properties.getBucketName();
        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            log.info("对象存储初始化完成，已创建存储桶：{}", bucket);
        } catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException e) {
            log.info("对象存储初始化完成，存储桶已存在：{}", bucket);
        }
        return s3Client;
    }

    @Bean
    public S3Presigner s3Presigner(OssProperties properties) {
        S3Presigner presigner = S3Presigner.builder()
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())
                        )
                )
                .serviceConfiguration(
                        S3Configuration.builder().pathStyleAccessEnabled(true).build()
                )
                .build();
        log.info("对象存储初始化完成，已创建预签名客户端");
        return presigner;
    }
}

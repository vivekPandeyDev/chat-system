package com.loop.troop.chat.application.service.file;

import com.loop.troop.chat.domain.upload.FileStorage;
import com.loop.troop.chat.domain.exception.FileServiceException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioFileStorage implements FileStorage {

    private final MinioClient minioClient;

    @Override
    public void initBucket(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception ex) {
            log.error("Failed to initialize bucket {}: {}", bucketName, ex.getMessage(), ex);
            throw FileServiceException.bucketInitFailed(bucketName, ex.getMessage());
        }
    }

    @Override
    public void upload(String bucketName, String objectName, InputStream inputStream, long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Failed to upload object {} to bucket {}: {}", objectName, bucketName, ex.getMessage(), ex);
            throw FileServiceException.uploadFailed(objectName, ex.getMessage());
        }
    }

    @Override
    public String generatePresignedUrl(String bucketName, String objectName, long expiry, TimeUnit unit) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry((int) unit.toSeconds(expiry), TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Failed to generate presigned URL for object {} in bucket {}: {}", 
                      objectName, bucketName, ex.getMessage(), ex);
            throw FileServiceException.presignedUrlFailed(objectName, ex.getMessage());
        }
    }
}

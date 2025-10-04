package com.loop.troop.chat.adapter.file;


import com.loop.troop.chat.web.exception.FileServiceException;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;
    private final Semaphore uploadSemaphore = new Semaphore(10);

    @Value("${app.config.bucket}")
    private String bucketName;

    @PostConstruct
    public void initBucket() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception ex) {
            log.error("Failed to initialize MinIO bucket: {}", ex.getMessage(), ex);
            throw FileServiceException.bucketInitFailed(bucketName, ex.getMessage());
        }
    }

    public void uploadProfileImage(String userId, MultipartFile file) throws InterruptedException {
        String objectName = "profile/" + userId + "/" + file.getOriginalFilename();
        uploadSemaphore.acquire();
        try (InputStream is = file.getInputStream()) {
            ObjectWriteResponse response = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("Uploaded profile image: {} for user {} | MinIO response: {}", objectName, userId, response);
        } catch (Exception ex) {
            log.error("Failed to upload profile image for user {} : {}", userId, ex.getMessage(), ex);
            throw FileServiceException.uploadFailed(userId, ex.getMessage());
        } finally {
            uploadSemaphore.release();
        }
    }

    public String getProfileImageUrl(String userId, String fileName) {
        String objectName = "profile/" + userId + "/" + fileName;
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Failed to generate profile image URL for user {} and file {}: {}",
                    userId, fileName, ex.getMessage(), ex);
            throw FileServiceException.presignedUrlFailed(fileName, ex.getMessage());
        }
    }

}

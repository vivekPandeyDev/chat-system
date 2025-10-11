package com.loop.troop.chat.infrastructure.jpa.adapter;

import com.loop.troop.chat.application.usecase.BucketStorage;
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
public class MinioBucketStorageAdapter implements BucketStorage {

	private final MinioClient minioClient;

	public void initBucket(String bucketName) {
		try {
			boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (!exists) {
				log.info("Initializing bucket with name: {}", bucketName);
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
			}
		}
		catch (Exception ex) {
			log.error("Failed to initialize bucket {}: {}", bucketName, ex.getMessage(), ex);
			throw FileServiceException.bucketInitFailed(bucketName, ex.getMessage());
		}
	}

	@Override
	public void uploadToBucket(String bucketName, String objectName, InputStream inputStream, long size,
			String contentType) {
		try {
			minioClient.putObject(PutObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.stream(inputStream, size, -1)
				.contentType(contentType)
				.build());
		}
		catch (Exception ex) {
			log.error("Failed to upload object {} to bucket {}: {}", objectName, bucketName, ex.getMessage(), ex);
			throw FileServiceException.uploadFailed(objectName, ex.getMessage());
		}
	}

	@Override
	public String generatePresignedUrl(String bucketName, String objectName, long expiry, TimeUnit unit) {
		try {
			return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
				.method(Method.GET)
				.bucket(bucketName)
				.object(objectName)
				.expiry((int) unit.toSeconds(expiry), TimeUnit.SECONDS)
				.build());
		}
		catch (Exception ex) {
			log.error("Failed to generate presigned URL for object {} in bucket {}: {}", objectName, bucketName,
					ex.getMessage(), ex);
			throw FileServiceException.presignedUrlFailed(objectName, ex.getMessage());
		}
	}

}

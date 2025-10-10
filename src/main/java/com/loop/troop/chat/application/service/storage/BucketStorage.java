package com.loop.troop.chat.application.service.storage;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public interface BucketStorage {
    void initBucket(String bucketName);
    void uploadToBucket(String bucketName, String objectName, InputStream inputStream, long size, String contentType);
    String generatePresignedUrl(String bucketName, String objectName, long expiry, TimeUnit unit);
}

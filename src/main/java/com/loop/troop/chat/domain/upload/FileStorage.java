package com.loop.troop.chat.domain.upload;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public interface FileStorage {
    void initBucket(String bucketName);
    void upload(String bucketName, String objectName, InputStream inputStream, long size, String contentType);
    String generatePresignedUrl(String bucketName, String objectName, long expiry, TimeUnit unit);
}

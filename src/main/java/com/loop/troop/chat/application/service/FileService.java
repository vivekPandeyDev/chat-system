package com.loop.troop.chat.application.service;

import com.loop.troop.chat.application.usecase.BucketStorage;
import com.loop.troop.chat.application.usecase.FileUseCase;
import com.loop.troop.chat.domain.exception.FileServiceException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements FileUseCase {

    private final BucketStorage bucketStorage;

    private final Semaphore uploadSemaphore = new Semaphore(10);

    @Value("${app.config.bucket}")
    private String bucketName;

    @PostConstruct
    public void initBucket() {
        log.info("Initialize the bucket with: {}",bucketName);
        bucketStorage.initBucket(bucketName);
    }

    @Override
    public void uploadFile(String filePath, InputStream inputStream, long size, String contentType) {
        try (InputStream is = inputStream) {
            uploadSemaphore.acquire();
            bucketStorage.uploadToBucket(bucketName, filePath, is, size, contentType);
            log.info("Uploaded file: {}", filePath);
        }catch (FileServiceException e) {
            // Already a domain-specific exception, just bubble up
            throw e;
        } catch (IOException e) {
            throw FileServiceException.inputStreamReadFailed(filePath, e.getMessage());
        } catch (Exception e){
            log.error("Unexpected failure during file upload for file {}: {}", filePath, e.getMessage(), e);
            throw FileServiceException.uploadFailed(filePath, e.getMessage());
        } finally {
            uploadSemaphore.release();
        }
    }

    @Override
    public String generatePresignedUrl(String fileName, long expiry, TimeUnit unit) {
        return bucketStorage.generatePresignedUrl(bucketName, fileName, 7, TimeUnit.DAYS);
    }
}

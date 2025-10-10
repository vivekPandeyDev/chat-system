package com.loop.troop.chat.application.service.file;

import com.loop.troop.chat.domain.upload.FileStorage;
import com.loop.troop.chat.domain.exception.FileServiceException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageManager {

    private final FileStorage fileStorage;
    private final Semaphore uploadSemaphore = new Semaphore(10);

    @Value("${app.config.bucket}")
    private String bucketName;

    @PostConstruct
    public void initBucket() {
        fileStorage.initBucket(bucketName);
    }

    public void uploadProfileImage(String userId, MultipartFile file) throws InterruptedException {
        String objectName = buildProfileObjectName(userId, file.getOriginalFilename());
        uploadSemaphore.acquire();
        try (InputStream is = file.getInputStream()) {
            fileStorage.upload(bucketName, objectName, is, file.getSize(), file.getContentType());
            log.info("Uploaded profile image {} for user {}", objectName, userId);
        }catch (FileServiceException e) {
            // Already a domain-specific exception, just bubble up
            throw e;
        } catch (IOException e) {
            throw FileServiceException.inputStreamReadFailed(file.getOriginalFilename(), e.getMessage());
        }catch (Exception e){
            log.error("Unexpected failure during file upload for user {}: {}", userId, e.getMessage(), e);
            throw FileServiceException.uploadFailed(userId, e.getMessage());
        } finally {
            uploadSemaphore.release();
        }
    }

    public String getProfileImageUrl(String userId, String fileName) {
        String objectName = buildProfileObjectName(userId, fileName);
        return fileStorage.generatePresignedUrl(bucketName, objectName, 7, TimeUnit.DAYS);
    }

    private String buildProfileObjectName(String userId, String fileName) {
        return "profile/" + userId + "/" + fileName;
    }
}

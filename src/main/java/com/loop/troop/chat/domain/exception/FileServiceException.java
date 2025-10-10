package com.loop.troop.chat.domain.exception;

import org.springframework.http.HttpStatus;

public class FileServiceException extends ServiceException {

    public FileServiceException(String errorCode, String userMessage, HttpStatus status) {
        super(errorCode, userMessage, status);
    }

    public static FileServiceException uploadFailed(String userId, String reason) {
        return new FileServiceException(
                "FILE_UPLOAD_FAILED",
                "Failed to upload profile image for user " + userId + ": " + reason,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static FileServiceException bucketInitFailed(String bucket, String reason) {
        return new FileServiceException(
                "BUCKET_INIT_FAILED",
                "Failed to initialize MinIO bucket: " + bucket + " (" + reason + ")",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static FileServiceException presignedUrlFailed(String fileName, String reason) {
        return new FileServiceException(
                "PRESIGNED_URL_FAILED",
                "Could not generate download URL for file " + fileName + ": " + reason,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static FileServiceException inputStreamReadFailed(String fileName, String reason) {
        return new FileServiceException(
                "FILE_INPUT_STREAM_FAILED",
                "Could not read file '" + fileName + "': " + reason,
                HttpStatus.BAD_REQUEST
        );
    }

}

package com.loop.troop.chat.domain.exception;

import org.springframework.http.HttpStatus;

import static com.loop.troop.chat.domain.constant.DomainConstant.*;

public class FileServiceException extends ServiceException {

	public FileServiceException(String errorCode, String userMessage, HttpStatus status) {
		super(errorCode, userMessage, status);
	}

	public static FileServiceException uploadFailed(String filePath, String reason) {
		return new FileServiceException(FILE_UPLOAD_FAILED.getValue(),
				"Failed to upload file from path " + filePath + ": " + reason, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static FileServiceException bucketInitFailed(String bucket, String reason) {
		return new FileServiceException(BUCKET_INIT_FAILED.getValue(),
				"Failed to initialize MinIO bucket: " + bucket + " (" + reason + ")", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static FileServiceException presignedUrlFailed(String filePath, String reason) {
		return new FileServiceException(PRESIGNED_URL_FAILED.getValue(),
				"Could not generate download URL for file from path " + filePath + ": " + reason,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static FileServiceException inputStreamReadFailed(String filePath, String reason) {
		return new FileServiceException(FILE_INPUT_STREAM_FAILED.getValue(),
				"Could not read file from path '" + filePath + "': " + reason, HttpStatus.BAD_REQUEST);
	}

}

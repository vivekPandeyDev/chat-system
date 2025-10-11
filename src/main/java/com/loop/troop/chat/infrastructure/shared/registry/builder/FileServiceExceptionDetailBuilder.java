package com.loop.troop.chat.infrastructure.shared.registry.builder;

import com.loop.troop.chat.domain.exception.FileServiceException;
import com.loop.troop.chat.domain.exception.builder.ServiceExceptionDetailBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileServiceExceptionDetailBuilder implements ServiceExceptionDetailBuilder<FileServiceException> {

	@Override
	public String buildDetail(FileServiceException exception) {
		return switch (exception.getErrorCode()) {
			case "FILE_UPLOAD_FAILED" -> "An error occurred while uploading a file. Please retry.";
			case "BUCKET_INIT_FAILED" -> "The storage bucket could not be initialized. Contact support.";
			case "PRESIGNED_URL_FAILED" -> "Download link generation failed. Please try again later.";
			default -> "Unexpected file observer error: " + exception.getUserMessage();
		};
	}

}

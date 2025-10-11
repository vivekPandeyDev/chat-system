package com.loop.troop.chat.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.InputStream;

public record FileUploadCommand(@NotBlank(message = "filePath cannot be blank") String filePath,
		@NotNull(message = "Content steam cannot be null") InputStream inputStream,
		@NotNull(message = "file size cannot be null") @Min(value = 1,
				message = "file size must be positive") Long size,
		@NotBlank(message = "filePath cannot be blank") String contentType) {
}

package com.loop.troop.chat.api.dto.file;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        @NotBlank String userId,
        MultipartFile file
) {}

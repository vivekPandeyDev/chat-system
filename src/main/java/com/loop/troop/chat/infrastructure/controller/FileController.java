package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.service.file.FileStorageManager;
import com.loop.troop.chat.infrastructure.shared.dto.file.FileUploadResponse;
import com.loop.troop.chat.infrastructure.shared.dto.file.FileUrlResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileStorageManager fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadProfileImage(
            @NotBlank(message = "User ID must not be blank") @RequestParam(value = "userId",required = false)  String userId,
            @NotNull(message = "File must be provided") @RequestParam(value = "file",required = false)  MultipartFile file
    ) throws InterruptedException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file cannot be empty");
        }
        fileService.uploadProfileImage(userId, file);
        return ResponseEntity.ok(
                new FileUploadResponse(file.getOriginalFilename(), "File uploaded successfully")
        );
    }

    @GetMapping("/profile-image")
    public ResponseEntity<FileUrlResponse> getProfileImageUrl(
            @NotBlank(message = "User ID must not be blank") @RequestParam(value = "userId",required = false)  String userId,
            @NotBlank(message = "File name must not be blank") @RequestParam(value = "fileName",required = false)  String fileName
    ) {
        String url = fileService.getProfileImageUrl(userId, fileName);
        return ResponseEntity.ok(new FileUrlResponse(url));
    }
}

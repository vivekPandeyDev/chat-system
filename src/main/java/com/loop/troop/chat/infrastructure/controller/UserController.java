package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.command.CreateUserCommand;
import com.loop.troop.chat.application.command.FileUploadCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.usecase.UserUseCase;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.user.CreateUserRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.user.UserResponseDto;
import com.loop.troop.chat.infrastructure.shared.mapper.UserMapper;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserUseCase userUserCase;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody CreateUserRequestDto request) {
		var user = userUserCase
			.registerUser(new CreateUserCommand(request.username(), request.email(), request.password()));
		var userFetchResponse = new ApiResponse<>(true, "User registered successfully",
				UserMapper.toResponseDto(user, userUserCase.fetchProfileUrl(user)));
		return ResponseEntity.ok(userFetchResponse);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable String userId) {
		var user = userUserCase.fetchUserByUserId(userId).orElseThrow(() -> UserServiceException.userNotFound(userId));
		var userFetchResponse = new ApiResponse<>(true, "User fetched successfully",
				UserMapper.toResponseDto(user, userUserCase.fetchProfileUrl(user)));
		return ResponseEntity.ok(userFetchResponse);
	}

	@GetMapping
	public ResponseEntity<PageResponse<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "ASC") String sortDir) {
		var query = new PaginationQuery(offset, size, sortBy, sortDir);
		var pageResponse = userUserCase.fetchUsers(query);
		var userResponseDtoList = pageResponse.content()
			.stream()
			.map(user -> UserMapper.toResponseDto(user, null))
			.toList();
		var pageResponseDto = new PageResponse<>(userResponseDtoList, pageResponse.totalPages(), pageResponse.size(),
				pageResponse.totalElements(), pageResponse.totalPages());
		return ResponseEntity.ok(pageResponseDto);
	}

	@PatchMapping("/{userId}/{status}")
	public ResponseEntity<ApiResponse<UserResponseDto>> updateStatus(@PathVariable String userId,
			@PathVariable String status) {
		UserStatus userStatus;
		try {
			userStatus = UserStatus.valueOf(status.toUpperCase());
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("valid user status required: ONLINE, OFFLINE, AWAY, DO_NOT_DISTURB");
		}
		userUserCase.updateStatus(userId, userStatus);
		var user = userUserCase.fetchUserByUserId(userId)
			.map(domain -> UserMapper.toResponseDto(domain, userUserCase.fetchProfileUrl(domain)))
			.orElseThrow(() -> UserServiceException.userNotFound(userId));
		var response = new ApiResponse<>(true, "User status updated successfully", user);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/upload/profile/{userId}")
	public ResponseEntity<ApiResponse<UserResponseDto>> uploadProfileImage(
			@ValidUUID(message = "User ID must be a valid id") @PathVariable(value = "userId",
					required = false) String userId,
			@NotNull(message = "File be provided") @RequestParam(value = "file", required = false) MultipartFile file)
			throws IOException {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("Uploaded file cannot be empty");
		}
		userUserCase.fetchUserByUserId(userId).orElseThrow(() -> UserServiceException.userNotFound(userId));
		var filePath = String.join("/", "profile", userId, Objects.requireNonNull(file.getOriginalFilename()));
		var fileUploadCommand = new FileUploadCommand(filePath, file.getInputStream(), file.getSize(),
				file.getContentType());
		var profileUrl = userUserCase.uploadUserProfile(userId, fileUploadCommand);
		var user = userUserCase.fetchUserByUserId(userId)
			.map(domain -> UserMapper.toResponseDto(domain, profileUrl))
			.orElseThrow(() -> UserServiceException.userNotFound(userId));
		var response = new ApiResponse<>(true, "User profile updated successfully", user);
		return ResponseEntity.ok(response);
	}

}

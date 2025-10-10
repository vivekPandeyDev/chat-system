package com.loop.troop.chat.infrastructure.controller;


import com.loop.troop.chat.application.command.CreateUserCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.service.user.UserService;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.user.CreateUserRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.user.UserResponseDto;
import com.loop.troop.chat.infrastructure.shared.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@RequestBody CreateUserRequestDto request) {
        var user = userService.registerUser(new CreateUserCommand(request.getUsername(), request.getEmail(), request.getAvatarUrl()));
        var userFetchResponse = new ApiResponse<>(true, "User registered successfully", UserMapper.toResponseDto(user));
        return ResponseEntity.ok(userFetchResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable String userId) {
        var user = userService.fetchUserByUserId(userId).orElseThrow(() -> UserServiceException.userNotFound(userId));
        var userFetchResponse = new ApiResponse<>(true, "User fetched successfully", UserMapper.toResponseDto(user));
        return ResponseEntity.ok(userFetchResponse);
    }


    @GetMapping
    public ResponseEntity<PageResponse<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        var query = new PaginationQuery(offset, size, sortBy, sortDir);
        var pageResponse = userService.fetchUsers(query);
        List<UserResponseDto> userResponseDtoList = pageResponse.content().stream().map(UserMapper::toResponseDto).toList();
        var pageResponseDto = new PageResponse<>(userResponseDtoList, pageResponse.totalPages(), pageResponse.size(), pageResponse.totalElements(), pageResponse.totalPages());
        return ResponseEntity.ok(pageResponseDto);
    }


    @PatchMapping("/{userId}/{status}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateStatus(@PathVariable String userId, @PathVariable String status) {
        UserStatus userStatus;
        try {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("valid user status required: ONLINE, OFFLINE, AWAY, DO_NOT_DISTURB");
        }
        userService.updateStatus(userId, userStatus);
        var user = userService.fetchUserByUserId(userId).map(UserMapper::toResponseDto).orElseThrow(() -> UserServiceException.userNotFound(userId));
        var response = new ApiResponse<>(true, "User status updated successfully", user);
        return ResponseEntity.ok(response);
    }
}

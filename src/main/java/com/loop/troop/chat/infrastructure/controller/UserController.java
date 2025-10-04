package com.loop.troop.chat.infrastructure.controller;


import com.loop.troop.chat.application.user.UserApplicationService;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.shared.dto.user.CreateUserRequestDto;
import com.loop.troop.chat.shared.dto.user.UserResponseDto;
import com.loop.troop.chat.shared.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userService;

    // ------------------------
    // Create a new user
    // ------------------------
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto request) {

        var user = userService.createUser(request);
        return ResponseEntity.ok(UserMapper.toResponseDto(user));
    }

    // ------------------------
    // Fetch user by ID
    // ------------------------
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String userId) {
        var user = userService.getUser(userId);
        return ResponseEntity.ok(UserMapper.toResponseDto(user));
    }

    // ------------------------
    // Fetch all users
    // ------------------------
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDto> response = users.stream()
                .map(UserMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    // ------------------------
    // Update user status
    // ------------------------
    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponseDto> updateStatus(@PathVariable String userId,
                                                        @RequestParam String status) {

        User updated = userService.updateStatus(userId, UserStatus.valueOf(status));
        return ResponseEntity.ok(UserMapper.toResponseDto(updated));
    }
}

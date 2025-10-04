package com.loop.troop.chat.application.user;

import com.loop.troop.chat.application.persistance.repo.UserRepository;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.shared.dto.user.CreateUserRequestDto;
import com.loop.troop.chat.shared.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(CreateUserRequestDto request) {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, request.getUsername(), request.getEmail());
        user.setAvatarUrl(request.getAvatarUrl());

        // Persist
        userRepository.save(UserMapper.toEntity(user));

        return user;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .map(UserMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Transactional
    public User updateStatus(String userId, UserStatus status) {
        var userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userEntity.setStatus(status);
        userRepository.save(userEntity);

        return UserMapper.toDomain(userEntity);
    }
}

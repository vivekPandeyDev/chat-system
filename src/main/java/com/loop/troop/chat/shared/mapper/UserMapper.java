package com.loop.troop.chat.shared.mapper;

import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.application.persistance.entity.UserEntity;

public class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        var user = new User(entity.getUserId(), entity.getUsername(), entity.getEmail());
        user.setAvatarUrl(entity.getAvatarUrl());
        user.setStatus(entity.getStatus());
        return user;
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserEntity.builder()
                .userId(domain.getUserId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .avatarUrl(domain.getAvatarUrl())
                .status(domain.getStatus())
                .build();
    }
}
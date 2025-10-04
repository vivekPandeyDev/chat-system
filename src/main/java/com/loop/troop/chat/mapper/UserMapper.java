package com.loop.troop.chat.mapper;

import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.adapter.persistance.entity.UserEntity;

public class UserMapper {

    private UserMapper() {
    }

    public static UserEntity toEntity(User domain) {
        return UserEntity.builder()
                .userId(domain.getUserId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .avatarUrl(domain.getAvatarUrl())
                .status(domain.getStatus())
                .build();
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity.getUserId(), entity.getUsername(), entity.getEmail());
    }
}
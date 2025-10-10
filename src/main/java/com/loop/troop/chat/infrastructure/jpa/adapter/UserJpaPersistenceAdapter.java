package com.loop.troop.chat.infrastructure.jpa.adapter;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.UserPersistence;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.infrastructure.jpa.repository.UserRepository;
import com.loop.troop.chat.infrastructure.shared.mapper.UserMapper;
import com.loop.troop.chat.infrastructure.shared.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.loop.troop.chat.infrastructure.shared.mapper.UserMapper.toDomain;
import static com.loop.troop.chat.infrastructure.shared.mapper.UserMapper.toEntity;

@Repository
@RequiredArgsConstructor
public class UserJpaPersistenceAdapter implements UserPersistence {
    private final UserRepository userRepository;
    @Override
    public User save(User user) {
        var savedUserEntity = userRepository.save(toEntity(user));
        return toDomain(savedUserEntity);
    }

    @Override
    public Optional<User> findById(String userId) {
        if (!Utility.isValidUUid(userId)){
            throw new IllegalArgumentException("Invalid UUID format");
        }
        return userRepository.findById(UUID.fromString(userId)).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (Utility.isBlank(email)){
            throw new IllegalArgumentException("email is required to check user availability");
        }
        return userRepository.existsByEmail(email);
    }

    @Override
    public PageResponse<User> findAll(PaginationQuery paginationQuery) {
        // Set defaults if null
        var page = paginationQuery.page() != null ? paginationQuery.page() : 0;
        var size = paginationQuery.size() != null ? paginationQuery.size() : 10;
        var sortBy = paginationQuery.sortBy() != null ? paginationQuery.sortBy() : "name";
        var direction = "desc".equalsIgnoreCase(paginationQuery.sortDir()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        var entityPage = userRepository.findAll(pageable);

        var users = entityPage.getContent().stream()
                .map(UserMapper::toDomain)
                .toList();

        return new PageResponse<>(
                users,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages()
        );
    }

    @Override
    public void updateStatus(String userId, UserStatus status) {
        if (!Utility.isValidUUid(userId)){
            throw new IllegalArgumentException("Invalid UUID format");
        }
        var savedUserEntity = userRepository.findById(UUID.fromString(userId)).orElseThrow(()-> UserServiceException.userNotFound(userId));
        savedUserEntity.setStatus(status);
        userRepository.save(savedUserEntity);
    }
}

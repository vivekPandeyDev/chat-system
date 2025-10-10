package com.loop.troop.chat.application.service.user;

import com.loop.troop.chat.application.event.UserRegisteredEvent;
import com.loop.troop.chat.application.event.UserStatusUpdateEvent;
import com.loop.troop.chat.application.command.CreateUserCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.UserPersistence;
import com.loop.troop.chat.application.usecase.UserUseCase;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

    private final UserPersistence userPersistence;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public User registerUser(@Valid CreateUserCommand command) {
        log.info("user registration command data: {}",command);
        try {
            if (userPersistence.existsByEmail(command.email())) {
                throw UserServiceException.userAlreadyExists(command.email());
            }
            var newUser = new User(command.username(), command.email(), command.avatarUrl());
            User savedUser = userPersistence.save(newUser);
            eventPublisher.publishEvent(new UserRegisteredEvent(savedUser));
            log.info("saved user info: {}", savedUser);
            return savedUser;
        }catch (Exception ex){
            log.info("Exception occurred while registering a new user: {}",ex.getMessage(),ex);
            throw   UserServiceException.registrationFailed(ex.getMessage());
        }
    }

    @Override
    public Optional<User> fetchUserByUserId(@NotBlank String userId) {
        log.info("user id to fetch user: {}",userId);
        return userPersistence.findById(userId);
    }

    @Override
    public PageResponse<User> fetchUsers(@Valid PaginationQuery query) {
        log.info("UserService::fetchUsers; page-offset: {}, page-size: {}, page-by: {}, page-dir: {}",query.page(),query.size(),query.sortBy(),query.sortDir());
        return userPersistence.findAll(query);
    }

    @Override
    public void updateStatus(@NotBlank String userId,@NotNull UserStatus status) {
        var savedUser = userPersistence.findById(userId).orElseThrow(() -> UserServiceException.userNotFound(userId));
        savedUser.updateStatus(status);
        log.info("UserService::fetchUsers, user-name: {}, current-status: {}",savedUser.getUsername(),status);
        savedUser = userPersistence.save(savedUser);
        eventPublisher.publishEvent(new UserStatusUpdateEvent(savedUser));
        log.info("UserService::fetchUsers, user-name: {}, current-status: {}",savedUser.getUsername(),status);
    }
}

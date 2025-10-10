package com.loop.troop.chat.application.usecase;

import com.loop.troop.chat.application.command.CreateUserCommand;
import com.loop.troop.chat.application.command.FileUploadCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;


public interface UserUseCase {
    User registerUser(@Valid CreateUserCommand command);
    Optional<User> fetchUserByUserId(@NotBlank String userId);
    PageResponse<User> fetchUsers(@Valid PaginationQuery paginationQuery);
    void updateStatus(@NotBlank String userId,@NotNull UserStatus status);
    String uploadUserProfile(@NotBlank String userId,@Valid FileUploadCommand command);
    String fetchProfileUrl(@NotNull User user);
    List<User> fetchUsersById(@NotNull List<String> userIds);
}

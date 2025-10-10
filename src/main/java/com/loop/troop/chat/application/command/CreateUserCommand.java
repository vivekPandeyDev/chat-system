package com.loop.troop.chat.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record CreateUserCommand(
        @NotBlank(message = "Username cannot be blank") String username,
        @NotBlank(message = "Email cannot be blank") @Email(message = "Email must be valid") String email,
        String avatarUrl
) {

}

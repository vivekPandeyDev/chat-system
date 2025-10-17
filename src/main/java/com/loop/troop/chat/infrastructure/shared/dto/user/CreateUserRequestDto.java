package com.loop.troop.chat.infrastructure.shared.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDto(

        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String email
) {
}

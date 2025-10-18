package com.loop.troop.chat.infrastructure.shared.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GenerateTokenRequest(

		@NotBlank(message = "Email cannot be blank") @Email(message = "Email must be valid") String email,

		@NotBlank(message = "Password cannot be blank") String password) {
}

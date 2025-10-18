package com.loop.troop.chat.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserCommand(

		@NotBlank(message = "Username cannot be blank") @Pattern(
				regexp = "^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$",
				message = "Username must be 3-20 characters, start with a letter, and may contain letters, numbers, dots, or underscores (no consecutive or trailing dots/underscores)") String username,

		@NotBlank(message = "Email cannot be blank") @Email(message = "Email must be valid") @Pattern(
				regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
				message = "Email must be a valid format (e.g. user@example.com)") String email,

		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
				message = "Password must be 8-20 characters long, include upper and lower case letters, a digit, and a special character") String password) {
}

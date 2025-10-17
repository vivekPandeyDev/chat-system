package com.loop.troop.chat.infrastructure.shared.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateTokenRequest {

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Email must be valid")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	private String password;

}

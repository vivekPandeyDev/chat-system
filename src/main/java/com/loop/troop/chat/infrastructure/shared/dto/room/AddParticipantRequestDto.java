package com.loop.troop.chat.infrastructure.shared.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddParticipantRequestDto {

	@NotBlank(message = "User ID is required")
	private String userId;

}

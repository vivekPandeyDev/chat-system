package com.loop.troop.chat.infrastructure.shared.dto.room;

import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddParticipantRequestDto {

	@NotBlank(message = "User ID is required")
	@ValidUUID
	private String userId;

}

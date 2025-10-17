package com.loop.troop.chat.infrastructure.shared.dto.message;

import com.loop.troop.chat.domain.enums.MessageType;
import com.loop.troop.chat.infrastructure.web.validator.EnumValue;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequestDto {

	@NotBlank(message = "Sender ID cannot be blank")
	@ValidUUID
	private String senderId;

	@NotBlank(message = "Content cannot be blank")
	private String content;

	@NotBlank(message = "Message Type is required")
	@EnumValue(enumClass = MessageType.class, message = "message type TEXT, IMAGE, VIDEO, FILE")
	private String messageType;

}

package com.loop.troop.chat.application.command;

import com.loop.troop.chat.domain.enums.MessageType;
import com.loop.troop.chat.infrastructure.web.validator.EnumValue;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import jakarta.validation.constraints.NotBlank;

public record CreateMessageCommand(@NotBlank(message = "room id cannot be blank") @ValidUUID String roomId,
		@NotBlank(message = "user id cannot be blank") @ValidUUID String userId,
		@NotBlank(message = "room id cannot be blank") String content,
		@NotBlank(message = "room id cannot be blank") @EnumValue(enumClass = MessageType.class,
				message = "valid message type : TEXT, IMAGE, VIDEO, FILE ") String messageType) {
}

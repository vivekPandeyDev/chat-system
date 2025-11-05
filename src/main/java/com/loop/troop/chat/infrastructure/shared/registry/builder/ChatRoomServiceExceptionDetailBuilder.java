package com.loop.troop.chat.infrastructure.shared.registry.builder;

import com.loop.troop.chat.domain.exception.ChatRoomServiceException;
import com.loop.troop.chat.domain.exception.builder.ServiceExceptionDetailBuilder;
import org.springframework.stereotype.Component;

import static com.loop.troop.chat.domain.constant.DomainConstant.fromValue;

@Component
public class ChatRoomServiceExceptionDetailBuilder implements ServiceExceptionDetailBuilder<ChatRoomServiceException> {

	@Override
	public String buildDetail(ChatRoomServiceException exception) {
		var code = fromValue(exception.getErrorCode());

		if (code == null) {
			return "Unexpected chat room observer error: " + exception.getUserMessage();
		}

		return switch (code) {
			case CHAT_ROOM_NOT_FOUND ->
				"The requested chat room could not be found. It may have been deleted or never existed.";
			case CHAT_ROOM_FOUND -> "A chat room with the same name already exists. Please choose a different name.";

			// fallback for other enum values
			default -> "Unexpected chat room observer error: " + exception.getUserMessage();
		};
	}

}

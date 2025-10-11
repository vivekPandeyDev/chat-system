package com.loop.troop.chat.infrastructure.shared.registry.builder;

import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.exception.builder.ServiceExceptionDetailBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceExceptionDetailBuilder implements ServiceExceptionDetailBuilder<UserServiceException> {

	@Override
	public String buildDetail(UserServiceException exception) {
		return switch (exception.getErrorCode()) {
			case "USER_NOT_FOUND" -> "The requested user could not be found. Please check the ID and try again.";
			case "USER_ALREADY_EXISTS" -> "A user with the same email already exists. Please use a different email.";
			case "USER_STATUS_UPDATE_FAILED" ->
				"Cannot update the status of this user. Verify the current status and try again.";
			case "USER_REGISTRATION_FAILED" ->
				"Registration could not be completed due to an internal error. Please try again later.";
			default -> "Unexpected user service error: " + exception.getUserMessage();
		};
	}

}

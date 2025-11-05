package com.loop.troop.chat.infrastructure.shared.registry.builder;

import com.loop.troop.chat.domain.constant.DomainConstant;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.exception.builder.ServiceExceptionDetailBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceExceptionDetailBuilder implements ServiceExceptionDetailBuilder<UserServiceException> {

	@Override
	public String buildDetail(UserServiceException exception) {
		var code = DomainConstant.fromValue(exception.getErrorCode());

		if (code == null) {
			return "Unexpected user observer error: " + exception.getUserMessage();
		}

		return switch (code) {
			case USER_EMAIL_NOT_FOUND, USER_NOT_FOUND -> "Please check your credentials and try again.";
			case USER_ALREADY_EXISTS -> "A user with the same email already exists. Please use a different email.";
			case USER_STATUS_UPDATE_FAILED ->
				"Cannot update the status of this user. Verify the current status and try again.";
			case USER_REGISTRATION_FAILED ->
				"Registration could not be completed due to an internal error. Please try again later.";

			// fallback for other enum values
			default -> "Unexpected user observer error: " + exception.getUserMessage();
		};
	}

}

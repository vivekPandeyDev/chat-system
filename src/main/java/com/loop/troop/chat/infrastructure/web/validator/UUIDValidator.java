package com.loop.troop.chat.infrastructure.web.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return false; // null or blank is invalid
		}
		try {
			UUID.fromString(value);
			return true;
		}
		catch (IllegalArgumentException ex) {
			return false;
		}
	}

}

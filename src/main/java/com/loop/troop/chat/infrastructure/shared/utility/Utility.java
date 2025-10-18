package com.loop.troop.chat.infrastructure.shared.utility;

import java.util.UUID;

public class Utility {

	private Utility() {
	}

	public static boolean isNotValidUUid(String value) {
		if (value == null || value.isBlank()) {
			return true;
		}
		try {
			UUID.fromString(value);
			return false;
		}
		catch (IllegalArgumentException ex) {
			return true;
		}
	}

	public static boolean isBlank(String value) {
		return value == null || value.isBlank(); // null or blank is invalid
	}

}

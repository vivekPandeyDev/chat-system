package com.loop.troop.chat.infrastructure.shared.utility;

import java.util.UUID;

public class Utility {

	private Utility() {
	}

	public static boolean isValidUUid(String value) {
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

	public static boolean isBlank(String value) {
		return value == null || value.isBlank(); // null or blank is invalid
	}

}

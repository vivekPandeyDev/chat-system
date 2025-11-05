package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Getter
@Setter
@Slf4j
@ToString
public class User {

	private String userId;

	private String username;

	private String email;

	private String imagePath;

	private UserStatus status;

	private String password;

	public User(String username, String email, String password) {
		this(username, email);
		this.password = password;
	}

	public User(String username, String email) {
		this.username = username;
		this.email = email;
		this.status = UserStatus.OFFLINE;
	}

	public void updateStatus(UserStatus status) {
		if (Objects.isNull(status)) {
			throw new IllegalStateException("User status is required");
		}
		this.status = status;
	}

}
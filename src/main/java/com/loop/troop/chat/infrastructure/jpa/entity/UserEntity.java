package com.loop.troop.chat.infrastructure.jpa.entity;

import com.loop.troop.chat.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users",
		indexes = { @Index(name = "idx_users_email", columnList = "email"),
				@Index(name = "idx_users_username", columnList = "username"),
				@Index(name = "idx_users_status", columnList = "status") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID userId;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	private String imagePath;
    private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;

}

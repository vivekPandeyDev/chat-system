package com.loop.troop.chat.infrastructure.jpa.entity;

import com.loop.troop.chat.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID notificationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private ChatRoomEntity room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "message_id")
	private MessageEntity message;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private boolean isRead;

	private LocalDateTime createdAt;

}

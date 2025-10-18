package com.loop.troop.chat.infrastructure.jpa.entity;

import com.loop.troop.chat.domain.enums.DeliveryStatus;
import com.loop.troop.chat.domain.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID messageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private ChatRoomEntity room;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private UserEntity sender;

	private String content;

	@Enumerated(EnumType.STRING)
	private MessageType type;

	private LocalDateTime sentAt;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

}

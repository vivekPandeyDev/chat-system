package com.loop.troop.chat.infrastructure.jpa.entity;

import com.loop.troop.chat.domain.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID roomId;

	@Enumerated(EnumType.STRING)
	private RoomType type;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private UserEntity createdBy;

	private LocalDateTime createdAt;

	private boolean isActive;

	private String groupName;

	private boolean isPermanent;

	private String imagePath;

	private LocalDateTime expiresAt;

	// Participants: used for both single and group message
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "chat_room_participant", joinColumns = @JoinColumn(name = "room_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<UserEntity> participants = new ArrayList<>();

	// Admins for group chats
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "chat_room_admin", joinColumns = @JoinColumn(name = "room_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<UserEntity> admins = new ArrayList<>();

}

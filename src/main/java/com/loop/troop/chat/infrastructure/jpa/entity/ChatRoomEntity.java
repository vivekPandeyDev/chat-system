package com.loop.troop.chat.infrastructure.jpa.entity;

import com.loop.troop.chat.domain.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomEntity {
    @Id
    private String roomId;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    private LocalDateTime createdAt;
    private boolean isActive;
    private String groupName;
    private boolean isPermanent;

    // Participants: used for both single and group chat
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chat_room_participants",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> participants = new ArrayList<>();

    // Admins for group chats
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chat_room_admins",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> admins = new ArrayList<>();
}

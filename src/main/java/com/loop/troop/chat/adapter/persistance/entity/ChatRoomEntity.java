package com.loop.troop.chat.adapter.persistance.entity;

import com.loop.troop.chat.domain.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
}

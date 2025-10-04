package com.loop.troop.chat.application.persistance.entity;


import com.loop.troop.chat.domain.enums.DeliveryStatus;
import com.loop.troop.chat.domain.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    @Id
    private String messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoomEntity room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}

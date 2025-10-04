package com.loop.troop.chat.shared.dto.message;


import com.loop.troop.chat.domain.enums.DeliveryStatus;
import com.loop.troop.chat.domain.enums.MessageType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponseDto {
    private String messageId;
    private String roomId;
    private String senderId;
    private String content;
    private MessageType type;
    private LocalDateTime sentAt;
    private DeliveryStatus status;
}

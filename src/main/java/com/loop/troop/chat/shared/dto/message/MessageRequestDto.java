package com.loop.troop.chat.shared.dto.message;

import com.loop.troop.chat.domain.enums.MessageType;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.web.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequestDto {

    @NotBlank(message = "Message ID cannot be blank")
    private String messageId;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotBlank(message = "Sender ID cannot be blank")
    private String senderId;

    @NotBlank(message = "Sender name cannot be blank")
    private String senderName;

    @NotBlank(message = "Sender email cannot be blank")
    private String senderEmail;

    @NotNull(message = "Message type is required")
    @EnumValue(enumClass = MessageType.class, message = "Invalid message type")
    private String messageType;

    @NotBlank(message = "Room type is required")
    @EnumValue(enumClass = RoomType.class, message = "Invalid room type")
    private String roomType; // "DIRECT" or "GROUP"
}

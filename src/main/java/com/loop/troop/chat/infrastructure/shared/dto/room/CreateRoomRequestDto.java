package com.loop.troop.chat.infrastructure.shared.dto.room;


import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.infrastructure.web.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateRoomRequestDto {

    @NotBlank(message = "Created by user ID is required")
    private String createdById;

    @NotNull(message = "Room type is required")
    @EnumValue(enumClass = RoomType.class, message = "Invalid room type")
    private String roomType;

    // Optional for group chat
    private String groupName;
    private Boolean isPermanent;
    // For single chat, must provide second participant
    private String otherParticipantId;

    // For group chat, optional list of initial participants
    private List<String> initialParticipantIds = new ArrayList<>();
}


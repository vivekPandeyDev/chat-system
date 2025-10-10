package com.loop.troop.chat.application.command;


import com.loop.troop.chat.domain.enums.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateChatRoomCommand{
    @NotBlank(message = "Created by user ID is required")
    private String createdById;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    // Optional for group chat
    private String groupName;
    private Boolean isPermanent;
    // For group chat, optional list of initial participants
    private List<String> initialParticipantIds = new ArrayList<>();

    // For single chat, must provide second participant
    private String otherParticipantId;


}

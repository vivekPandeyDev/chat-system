package com.loop.troop.chat.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateGroupChatRoomCommand(
        @NotBlank(message = "Created by user ID is required") String createdById,

        @NotBlank(message = "Group name is required") @Size(min = 3, max = 50,
                message = "Group name must be between 3 and 50 characters") @Pattern(regexp = "^[a-zA-Z0-9 _-]+$",
                message = "Group name can only contain letters, numbers, spaces, hyphens, and underscores") String groupName,

        boolean isPermanent,

        List<@NotBlank(message = "Participant ID cannot be blank") String> participantIds) {

    public CreateGroupChatRoomCommand {
        if (participantIds == null) {
            participantIds = List.of();
        }
    }

    public CreateGroupChatRoomCommand(String createdById, String groupName, List<String> participantIds) {
        this(createdById, groupName, true, participantIds);
    }
}

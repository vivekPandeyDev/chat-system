package com.loop.troop.chat.application.command;

import jakarta.validation.constraints.NotBlank;

public record CreateSingleChatRoomCommand(@NotBlank(message = "user id cannot be blank") String createdById,@NotBlank(message = "user id cannot be blank") String otherParticipantsId) {
}

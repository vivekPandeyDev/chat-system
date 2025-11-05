package com.loop.troop.chat.application.command;

import com.loop.troop.chat.domain.User;

public record SingleChatRoomSaveCommand(User createdBy, User otherParticipant) {
}
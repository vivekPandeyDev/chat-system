package com.loop.troop.chat.infrastructure.shared.dto.room;

import java.util.List;

public record CreateGroupChatRoomRequest(
        String createdById,
        String groupName,
        Boolean isPermanent,
        List<String> initialParticipantIds
) {
}
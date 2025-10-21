package com.loop.troop.chat.infrastructure.shared.dto.room;

public record FetchChatRoomByUserResponse(String groupId, String roomType,String groupName, String groupImageUrl,String lastTextMessage) {
}

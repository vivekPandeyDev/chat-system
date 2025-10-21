package com.loop.troop.chat.infrastructure.shared.mapper;

import com.loop.troop.chat.application.projection.ChatRoomProjection;
import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.GroupChatRoom;
import com.loop.troop.chat.domain.SingleChatRoom;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity;
import com.loop.troop.chat.infrastructure.jpa.repository.ChatRoomEntityInfo;
import com.loop.troop.chat.infrastructure.shared.dto.room.ChatRoomResponseDto;
import com.loop.troop.chat.infrastructure.shared.dto.room.FetchChatRoomByUserResponse;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ChatRoomMapper {

	private ChatRoomMapper() {
	}

	public static ChatRoom toDomain(ChatRoomEntity entity) {
		if (entity == null)
			return null;

		var creator = UserMapper.toDomain(entity.getCreatedBy());
		var participants = entity.getParticipants().stream().filter(userEntity -> !userEntity.getUserId().equals(entity.getCreatedBy().getUserId()) ).map(UserMapper::toDomain).toList();
        var admins = entity.getAdmins().stream().map(UserMapper::toDomain).toList();
		ChatRoom room;
		if (entity.getType() == RoomType.SINGLE) {
			if (participants.isEmpty()) {
				throw new IllegalArgumentException("Must have other participant for single message room");
			}
			room = new SingleChatRoom(toString(entity.getRoomId()), creator, participants.getFirst());
		}
		else {
			room = new GroupChatRoom(toString(entity.getRoomId()), creator, entity.getGroupName(),
					entity.isPermanent(), participants,admins);
		}

        room.setActive(entity.isActive());
        room.setImagePath(entity.getImagePath());
		return room;
	}

	public static ChatRoomEntity toEntity(ChatRoom domain) {
		if (domain == null)
			return null;

		var builder = ChatRoomEntity.builder()
			.roomId(toUuid(domain.getRoomId()))
			.type(domain.getType())
			.createdBy(UserMapper.toEntity(domain.getCreatedBy()))
			.createdAt(domain.getCreatedAt())
			.isActive(domain.isActive());

		// map participants
		builder.participants(domain.getParticipants().stream().map(UserMapper::toEntity).toList());

		if (domain instanceof GroupChatRoom g) {

			builder.isPermanent(g.isPermanent());
			builder.admins(g.getAdmins().stream().map(UserMapper::toEntity).toList());
		}else{
            builder.admins(new ArrayList<>());
        }
        builder.imagePath(domain.getImagePath());
        builder.groupName(domain.getRoomName());
		return builder.build();
	}

	public static ChatRoomResponseDto chatRoomResponseDto(ChatRoom chatRoom) {
		if (chatRoom == null) {
			return null;
		}
		ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
		chatRoomResponseDto.setRoomId(toUuid(chatRoom.getRoomId()));
		chatRoomResponseDto.setCreatedBy(UserMapper.toResponseDto(chatRoom.getCreatedBy(), null));
		chatRoomResponseDto.setType(String.valueOf(chatRoom.getType()));
		chatRoomResponseDto.setParticipants(
				chatRoom.getParticipants().stream().map(user -> UserMapper.toResponseDto(user, null)).toList());
		chatRoomResponseDto.setCreatedAt(chatRoom.getCreatedAt());
		chatRoomResponseDto.setActive(chatRoom.isActive());
		if (chatRoom instanceof GroupChatRoom groupChatRoom) {

			chatRoomResponseDto.setPermanent(groupChatRoom.isPermanent());
			chatRoomResponseDto.setAdmins(
					groupChatRoom.getAdmins().stream().map(user -> UserMapper.toResponseDto(user, null)).toList());
		}
        chatRoomResponseDto.setRoomName(chatRoom.getRoomName());
		return chatRoomResponseDto;
	}

    public static ChatRoomProjection chatRoomProjection(ChatRoomEntityInfo entityInfo){
        return new ChatRoomProjection(toString(entityInfo.getRoomId()),entityInfo.getType().name(),entityInfo.getGroupName(), entityInfo.getImagePath());
    }

    public static FetchChatRoomByUserResponse chatRoomProjectionResponse(ChatRoomProjection projection,String groupImageUrl,String lastTextMessage){
        return new FetchChatRoomByUserResponse(projection.getGroupId(),projection.getRoomType(),projection.getRoomName(), groupImageUrl,lastTextMessage);
    }

	public static String toString(UUID uuid) {
		if (Objects.nonNull(uuid)) {
			return uuid.toString();
		}
		else {
			return null;
		}
	}

	public static UUID toUuid(String id) {
		if (Objects.nonNull(id)) {
			return UUID.fromString(id);
		}
		else {
			return null;
		}
	}

}

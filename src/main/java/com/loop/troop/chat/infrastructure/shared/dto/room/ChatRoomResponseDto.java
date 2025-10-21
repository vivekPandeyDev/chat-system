package com.loop.troop.chat.infrastructure.shared.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loop.troop.chat.infrastructure.shared.dto.user.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ChatRoomResponseDto {

	private UUID roomId;

	private String type;

	private UserResponseDto createdBy;

	private LocalDateTime createdAt;

	private List<UserResponseDto> participants = new ArrayList<>();

	private boolean active;
    private String roomName;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean isPermanent;



	private List<UserResponseDto> admins = new ArrayList<>();

}
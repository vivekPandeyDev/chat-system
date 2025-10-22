package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.command.CreateGroupChatRoomCommand;
import com.loop.troop.chat.application.usecase.GroupChatRoomUseCase;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.room.AddParticipantRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.room.CreateGroupChatRoomRequest;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group/room")
@RequiredArgsConstructor
@Validated
public class GroupChatRoomController {

	private final GroupChatRoomUseCase groupChatRoomUseCase;

	@PostMapping
	public ResponseEntity<ApiResponse<String>> createGroupChatRoom(@RequestBody CreateGroupChatRoomRequest request) {
		request.initialParticipantIds().add(request.createdById());
		var groupChatRoomCommand = new CreateGroupChatRoomCommand(request.createdById(), request.groupName(),
				request.isPermanent(), request.initialParticipantIds());
		String roomId = groupChatRoomUseCase.createGroupChatRoom(groupChatRoomCommand);
		var chatRoomCreatedResponse = new ApiResponse<>(true, "new message room created", roomId);
		return ResponseEntity.ok(chatRoomCreatedResponse);
	}

	@PostMapping("/{roomId}/participants")
	public ResponseEntity<Void> addParticipant(@ValidUUID @PathVariable String roomId,
			@Valid @RequestBody AddParticipantRequestDto request) {

		groupChatRoomUseCase.addParticipants(roomId, request.getUserId());
		return ResponseEntity.accepted().build();
	}

	@DeleteMapping("/{roomId}/participants/{userId}")
	public ResponseEntity<String> removeParticipant(@ValidUUID @PathVariable String roomId,
			@ValidUUID @PathVariable String userId) {
		groupChatRoomUseCase.removeParticipants(roomId, userId);
		return ResponseEntity.noContent().build();
	}

}

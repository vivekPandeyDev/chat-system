package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.command.CreateChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.usecase.ChatRoomUseCase;
import com.loop.troop.chat.application.usecase.GroupChatRoomUseCase;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.room.AddParticipantRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.room.ChatRoomResponseDto;
import com.loop.troop.chat.infrastructure.shared.dto.room.CreateRoomRequestDto;
import com.loop.troop.chat.infrastructure.shared.mapper.ChatRoomMapper;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Validated
public class ChatRoomController {

	private final ChatRoomUseCase chatRoomUseCase;

	private final GroupChatRoomUseCase groupChatRoomUseCase;

	@PostMapping
	public ResponseEntity<ApiResponse<String>> createRoom(@Valid @RequestBody CreateRoomRequestDto request) {
		String roomId = chatRoomUseCase.createRoom(roomCommand(request));
		var chatRoomCreatedResponse = new ApiResponse<>(true, "new message room created", roomId);
		return ResponseEntity.ok(chatRoomCreatedResponse);
	}

	private CreateChatRoomCommand roomCommand(CreateRoomRequestDto request) {
		return CreateChatRoomCommand.builder()
			.roomType(RoomType.valueOf(request.getRoomType()))
			.createdById(request.getCreatedById())
			.groupName(request.getGroupName())
			.initialParticipantIds(request.getInitialParticipantIds())
			.otherParticipantId(request.getOtherParticipantId())
			.isPermanent(request.getIsPermanent())
			.build();
	}

	@GetMapping
	public ResponseEntity<PageResponse<ChatRoomResponseDto>> getAllRooms(
			@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "ASC") String sortDir) {
		var query = new PaginationQuery(offset, size, sortBy, sortDir);
		var pageResponse = chatRoomUseCase.fetchChatRoom(query);
		var pageResponseDto = new PageResponse<>(
				pageResponse.content().stream().map(ChatRoomMapper::chatRoomResponseDto).toList(),
				pageResponse.totalPages(), pageResponse.size(), pageResponse.totalElements(),
				pageResponse.totalPages());
		return ResponseEntity.ok(pageResponseDto);
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

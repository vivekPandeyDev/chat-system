package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.command.CreateSingleChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.usecase.ChatRoomUseCase;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.room.ChatRoomResponseDto;
import com.loop.troop.chat.infrastructure.shared.dto.room.CreateSingleChatRoomRequest;
import com.loop.troop.chat.infrastructure.shared.mapper.ChatRoomMapper;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
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

	@GetMapping("/{userId}")
	public ResponseEntity<PageResponse<ChatRoomResponseDto>> getAvailableChatRoomForUser(
			@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "ASC") String sortDir,
            @ValidUUID @PathVariable String userId
        ) {
		var query = new PaginationQuery(offset, size, sortBy, sortDir);
		var pageResponse = chatRoomUseCase.fetchChatRoomPerUser(query,userId);
		var pageResponseDto = new PageResponse<>(
				pageResponse.content().stream().map(ChatRoomMapper::chatRoomResponseDto).toList(),
				pageResponse.totalPages(), pageResponse.size(), pageResponse.totalElements(),
				pageResponse.totalPages());
		return ResponseEntity.ok(pageResponseDto);
	}

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createSingleChatRoom(@RequestBody CreateSingleChatRoomRequest request){
        var roomId = chatRoomUseCase.createSingleChatRoom(new CreateSingleChatRoomCommand(request.createdById(),request.otherParticipantsId()));
        var chatRoomCreatedResponse = new ApiResponse<>(true, "Single chat room created", roomId);
        return ResponseEntity.ok(chatRoomCreatedResponse);
    }

}

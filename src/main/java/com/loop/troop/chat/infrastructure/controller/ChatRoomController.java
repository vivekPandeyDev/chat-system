package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.command.CreateSingleChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.usecase.ChatRoomUseCase;
import com.loop.troop.chat.application.usecase.FileUseCase;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.room.ChatRoomResponseDto;
import com.loop.troop.chat.infrastructure.shared.dto.room.CreateSingleChatRoomRequest;
import com.loop.troop.chat.infrastructure.shared.dto.room.FetchChatRoomByUserResponse;
import com.loop.troop.chat.infrastructure.shared.mapper.ChatRoomMapper;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Validated
public class ChatRoomController {

	private final ChatRoomUseCase chatRoomUseCase;
    private final FileUseCase fileUseCase;

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

    @GetMapping("/projection/{userId}")
    public ResponseEntity<PageResponse<FetchChatRoomByUserResponse>> getAvailableChatRoomProjectionForUser(
            @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir,
            @ValidUUID @PathVariable String userId
    ) {
        var query = new PaginationQuery(offset, size, sortBy, sortDir);
        var pageResponse = chatRoomUseCase.fetchChatRoomProjectionPerUser(query,userId);
        var pageResponseDto = new PageResponse<>(
                pageResponse.content().stream().map(projection -> {
                    String imageUrl =null;
                    if (Objects.nonNull(projection.getImagePath()))
                        imageUrl = fileUseCase.generatePresignedUrl(projection.getImagePath(),7, TimeUnit.DAYS);
                    // TODO -> message use case to fetch latest text message
                    return ChatRoomMapper.chatRoomProjectionResponse(projection,imageUrl,"No message currently");
                }).toList(),
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

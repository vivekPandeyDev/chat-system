package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.command.CreateMessageCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.usecase.MessageUseCase;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageResponseDto;
import com.loop.troop.chat.infrastructure.shared.mapper.MessageMapper;
import com.loop.troop.chat.infrastructure.web.validator.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class ChatController {

	private final MessageUseCase messageUseCase;

	@PostMapping("/{roomId}/message")
	public ResponseEntity<ApiResponse<MessageResponseDto>> sendMessage(@ValidUUID @PathVariable String roomId,
			@Valid @RequestBody MessageRequestDto request) {
		var createdMessage = messageUseCase.createMessage(new CreateMessageCommand(roomId, request.getSenderId(),
				request.getContent(), request.getMessageType()));
		messageUseCase.sendMessage(createdMessage);
		var messageSentResponse = new ApiResponse<>(true, "new message send",
				MessageMapper.toResponseDto(createdMessage));
		return ResponseEntity.ok(messageSentResponse);
	}

	@DeleteMapping("/{roomId}/message/{messageId}")
	public ResponseEntity<Void> deleteMessage(@ValidUUID @PathVariable String roomId,
			@ValidUUID @PathVariable String messageId) {
		messageUseCase.deleteMessge(roomId, messageId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{roomId}/message")
	public ResponseEntity<PageResponse<MessageResponseDto>> fetchMessages(@PathVariable String roomId,
			@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(defaultValue = "sentAt") String sortBy, @RequestParam(defaultValue = "ASC") String sortDir) {
		var query = new PaginationQuery(offset, size, sortBy, sortDir);
		var pageResponse = messageUseCase.fetchMessageByRoomId(roomId, query);
		var pageResponseDto = new PageResponse<>(
				pageResponse.content().stream().map(MessageMapper::toResponseDto).toList(), pageResponse.page(),
				pageResponse.size(), pageResponse.totalElements(), pageResponse.totalPages());
		return ResponseEntity.ok(pageResponseDto);
	}

}

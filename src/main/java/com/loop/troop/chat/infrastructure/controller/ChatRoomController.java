package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.chat.ChatRoomApplicationService;
import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.infrastructure.shared.dto.room.AddParticipantRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.room.CreateRoomRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Validated
public class ChatRoomController {

	private final ChatRoomApplicationService roomService;

	// ------------------------
	// Create a chat room
	// ------------------------
	@PostMapping
	public ResponseEntity<String> createRoom(@Valid @RequestBody CreateRoomRequestDto request) {
		try {
			String roomId = roomService.createRoom(request);
			return ResponseEntity.ok("Room created successfully with ID: " + roomId);
		}
		catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	// ------------------------
	// Fetch all rooms
	// ------------------------
	@GetMapping
	public ResponseEntity<List<String>> getAllRooms() {
		List<ChatRoom> rooms = roomService.getAllRooms();
		List<String> roomIds = rooms.stream().map(ChatRoom::getRoomId).toList();
		return ResponseEntity.ok(roomIds);
	}

	// ------------------------
	// Add participant to a room
	// ------------------------
	@PostMapping("/{roomId}/participants")
	public ResponseEntity<String> addParticipant(@PathVariable String roomId,
			@Valid @RequestBody AddParticipantRequestDto request) {

		roomService.addParticipant(roomId, request.getUserId());

		return ResponseEntity.ok("Participant added successfully");
	}

	// ------------------------
	// Remove participant from a room
	// ------------------------
	@DeleteMapping("/{roomId}/participants/{userId}")
	public ResponseEntity<String> removeParticipant(@PathVariable String roomId, @PathVariable String userId) {

		roomService.removeParticipant(roomId, userId);

		return ResponseEntity.ok("Participant removed successfully");
	}

}

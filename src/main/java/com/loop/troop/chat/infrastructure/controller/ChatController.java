// package com.loop.troop.message.infrastructure.controller;
//
// import com.loop.troop.message.application.message.ChatRoomApplicationService;
// import com.loop.troop.message.application.observer.message.DirectChatService;
// import com.loop.troop.message.application.observer.message.GroupChatService;
// import com.loop.troop.message.application.observer.UserService;
// import com.loop.troop.message.application.usecase.ChatRoomUseCase;
// import com.loop.troop.message.domain.ChatRoom;
// import com.loop.troop.message.domain.enums.MessageType;
// import com.loop.troop.message.domain.enums.RoomType;
// import com.loop.troop.message.domain.exception.ChatRoomServiceException;
// import com.loop.troop.message.domain.exception.UserServiceException;
// import com.loop.troop.message.domain.Message;
// import com.loop.troop.message.domain.observer.ChatService;
// import com.loop.troop.message.infrastructure.shared.dto.message.MessageRequestDto;
// import com.loop.troop.message.infrastructure.shared.dto.message.MessageResponseDto;
// import com.loop.troop.message.infrastructure.shared.mapper.MessageMapper;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.List;
// import java.util.UUID;
//
// @RestController
// @RequestMapping("/api/message")
// @RequiredArgsConstructor
// public class ChatController {
//
// private final DirectChatService directChatService;
//
// private final GroupChatService groupChatService;
//
// private final ChatRoomUseCase chatRoomUseCase;
//
// private final UserService userService;
//
// // Send a message
// @PostMapping("/{roomId}/message")
// public ResponseEntity<MessageResponseDto> sendMessage(@PathVariable String roomId,
// @Valid @RequestBody MessageRequestDto request) {
// // Build sender domain object
// var sender = userService.fetchUserByUserId(request.getSenderId()).orElseThrow(() ->
// UserServiceException.userNotFound(request.getSenderId()));
//
// var room = chatRoomUseCase.getChatRoomById(roomId).orElseThrow(() ->
// ChatRoomServiceException.roomNotFound(roomId));
//
// // Build message domain object
// Message msg = new Message(UUID.randomUUID().toString(), room, sender,
// request.getContent(),
// MessageType.valueOf(request.getMessageType()));
// sender.sendMessage(room, msg);
//
// // Select proper message observer
// ChatService svc = room.getType().equals(RoomType.GROUP) ? groupChatService :
// directChatService;
//
// svc.sendMessage(msg);
//
// return ResponseEntity.ok(MessageMapper.toResponseDto(msg));
// }
//
// // Fetch messages
// @GetMapping("/{roomId}/messages")
// public ResponseEntity<List<MessageResponseDto>> fetchMessages(@PathVariable String
// roomId,
// @RequestParam(defaultValue = "DIRECT") String roomType) {
// ChatService svc = roomType.equalsIgnoreCase("GROUP") ? groupChatService :
// directChatService;
// List<Message> messages = svc.fetchMessages(roomId);
//
// List<MessageResponseDto> response =
// messages.stream().map(MessageMapper::toResponseDto).toList();
//
// return ResponseEntity.ok(response);
// }
//
// }

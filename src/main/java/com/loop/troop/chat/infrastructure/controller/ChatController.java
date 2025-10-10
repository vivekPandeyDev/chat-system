package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.chat.ChatRoomApplicationService;
import com.loop.troop.chat.application.service.DirectChatService;
import com.loop.troop.chat.application.service.GroupChatService;
import com.loop.troop.chat.application.service.UserService;
import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.enums.MessageType;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.domain.service.ChatService;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageResponseDto;
import com.loop.troop.chat.infrastructure.shared.mapper.MessageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final DirectChatService directChatService;
    private final GroupChatService groupChatService;
    private final ChatRoomApplicationService chatRoomApplicationService;
    private final UserService userService;
    // Send a message
    @PostMapping("/{roomId}/message")
    public ResponseEntity<MessageResponseDto> sendMessage(
            @PathVariable String roomId,
            @Valid @RequestBody MessageRequestDto request
    ) {
        // Build sender domain object
        var sender = userService.fetchUserByUserId(request.getSenderId()).orElseThrow(() -> UserServiceException.userNotFound(request.getSenderId()));

        // Fetch or create domain ChatRoom (simplified here, ideally fetch from DB)
        ChatRoom room = chatRoomApplicationService.getRoomById(roomId);

        // Build message domain object
        Message msg = new Message(
                UUID.randomUUID().toString(),
                room,
                sender,
                request.getContent(),
                MessageType.valueOf(request.getMessageType())
        );
        sender.sendMessage(room,msg);

        // Select proper chat service
        ChatService svc = room.getType().equals(RoomType.GROUP) ? groupChatService : directChatService;

        svc.sendMessage(msg);

        return ResponseEntity.ok(MessageMapper.toResponseDto(msg));
    }

    // Fetch messages
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<MessageResponseDto>> fetchMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "DIRECT") String roomType
    ) {
        ChatService svc = roomType.equalsIgnoreCase("GROUP") ? groupChatService : directChatService;
        List<Message> messages = svc.fetchMessages(roomId);

        List<MessageResponseDto> response = messages.stream()
                .map(MessageMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}

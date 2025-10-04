package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.chat.ChatRoomApplicationService;
import com.loop.troop.chat.application.message.DirectChatSvc;
import com.loop.troop.chat.application.message.GroupChatSvc;
import com.loop.troop.chat.domain.chat.ChatRoom;
import com.loop.troop.chat.domain.enums.MessageType;
import com.loop.troop.chat.domain.message.Message;
import com.loop.troop.chat.domain.service.ChatService;
import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.shared.dto.message.MessageRequestDto;
import com.loop.troop.chat.shared.dto.message.MessageResponseDto;
import com.loop.troop.chat.shared.mapper.MessageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final DirectChatSvc directChatSvc;
    private final GroupChatSvc groupChatSvc;
    private final ChatRoomApplicationService chatRoomApplicationService;

    // Send a message
    @PostMapping("/{roomId}/message")
    public ResponseEntity<MessageResponseDto> sendMessage(
            @PathVariable String roomId,
            @Valid @RequestBody MessageRequestDto request
    ) {
        // Build sender domain object
        User sender = new User(request.getSenderId(), request.getSenderName(), request.getSenderEmail());

        // Fetch or create domain ChatRoom (simplified here, ideally fetch from DB)
        ChatRoom room = chatRoomApplicationService.getRoomById(roomId);

        // Build message domain object
        Message msg = new Message(
                request.getMessageId(),
                room,
                sender,
                request.getContent(),
                MessageType.valueOf(request.getMessageType())
        );
        sender.sendMessage(room,msg);

        // Select proper chat service
        ChatService svc = request.getRoomType().equalsIgnoreCase("GROUP") ? groupChatSvc : directChatSvc;

        svc.sendMessage(msg);

        return ResponseEntity.ok(MessageMapper.toResponseDto(msg));
    }

    // Fetch messages
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<MessageResponseDto>> fetchMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "DIRECT") String roomType
    ) {
        ChatService svc = roomType.equalsIgnoreCase("GROUP") ? groupChatSvc : directChatSvc;
        List<Message> messages = svc.fetchMessages(roomId);

        List<MessageResponseDto> response = messages.stream()
                .map(MessageMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}

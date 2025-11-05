package com.loop.troop.chat.infrastructure.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loop.troop.chat.application.command.CreateMessageCommand;
import com.loop.troop.chat.application.usecase.MessageUseCase;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageRequestDto;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

	private final Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();

	private final MessageUseCase messageUseCase;

	private final ObjectMapper mapper;

	@Override
	public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
		String roomId = getRoomId(session);
		sessions.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(session);
		log.info("Connection opened. Room: {}, Active users: {}", roomId,
				sessions.getOrDefault(roomId, List.of()).size());
	}

	@Override
	protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
		String roomId = getRoomId(session);
		// TODO -> send to messaging queue
		sendToRoom(roomId, message.getPayload());
	}

	@Override
	public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
		String roomId = getRoomId(session);
		sessions.computeIfPresent(roomId, (key, list) -> {
			list.remove(session);
			return list.isEmpty() ? null : list; // if empty, remove the room key
		});

		log.info("Connection closed. Room: {}, Active users: {}", roomId,
				sessions.getOrDefault(roomId, List.of()).size());
	}

	public void sendToRoom(String roomId, String msg) throws JsonProcessingException {
		var currentRoomSessions = sessions.getOrDefault(roomId, new ArrayList<>());
		var messageRequestDto = mapper.readValue(msg, MessageRequestDto.class);
		var createdMessage = messageUseCase.createMessage(new CreateMessageCommand(roomId,
				messageRequestDto.getSenderId(), messageRequestDto.getContent(), messageRequestDto.getMessageType()));
		var messageResponse = MessageResponseDto.builder()
			.messageId(createdMessage.getMessageId())
			.type(createdMessage.getType())
			.roomId(createdMessage.getRoom().getRoomId())
			.sentAt(createdMessage.getSentAt())
			.status(createdMessage.getStatus())
			.content(createdMessage.getContent())
			.senderId(createdMessage.getSender().getUserId())
			.build();
		messageUseCase.sendMessage(createdMessage);
		var messageResponseString = mapper.writeValueAsString(messageResponse);
		currentRoomSessions.stream()
			.filter(WebSocketSession::isOpen)
			.forEach(session -> sendMessage(session, roomId, messageResponseString));

	}

	public static void sendMessage(WebSocketSession session, String roomId, String msg) {
		log.info("text message content: {}, for particular room-id: {}", roomId, msg);
		try {
			session.sendMessage(new TextMessage(msg));
		}
		catch (IOException e) {
			log.error("Error in sending message: {}", e.getMessage(), e);
		}
	}

	public static void sendMessage(WebSocketSession session, String msg) {
		log.info("text message content for all roomId : {}", msg);
		try {
			session.sendMessage(new TextMessage(msg));
		}
		catch (IOException e) {
			log.error("Error in sending message: {}", e.getMessage(), e);
		}
	}

	public void broadcast(String msg) {
		for (var broadCastSessions : sessions.values()) {
			broadCastSessions.stream().filter(WebSocketSession::isOpen).forEach(session -> sendMessage(session, msg));
		}
	}

	private String getRoomId(WebSocketSession session) {
		var path = Objects.requireNonNull(session.getUri()).getPath(); // e.g.
																		// /ws/chat/room123
		var roomId = path.substring(path.lastIndexOf("/") + 1);
		session.getAttributes().put("roomId", roomId);
		return roomId;
	}

}

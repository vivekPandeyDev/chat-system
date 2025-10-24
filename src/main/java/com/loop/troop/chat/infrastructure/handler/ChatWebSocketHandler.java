package com.loop.troop.chat.infrastructure.handler;

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
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        String roomId = getRoomId(session);
        sessions.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>())
                .add(session);
        log.info("Connection opened. Room: {}, Active users: {}", roomId, sessions.getOrDefault(roomId, List.of()).size());
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomId(session);
        String payload = roomId + ":" + message.getPayload();
        // TODO -> send to messaging queue
        sendToRoom(roomId,payload);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        String roomId = getRoomId(session);
        sessions.computeIfPresent(roomId, (key, list) -> {
            list.remove(session);
            return list.isEmpty() ? null : list; // if empty, remove the room key
        });

        log.info("Connection closed. Room: {}, Active users: {}", roomId, sessions.getOrDefault(roomId, List.of()).size());
    }

    public void sendToRoom(String roomId, String msg) {
        var currentRoomSessions = sessions.getOrDefault(roomId, new ArrayList<>());
        currentRoomSessions.stream().filter(WebSocketSession::isOpen).forEach(session -> sendMessage(session,msg));

    }

    public  static void sendMessage(WebSocketSession session,String msg){
        try {
            session.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            log.error("Error in sending message: {}",e.getMessage(),e);
        }
    }

    public void broadcast(String msg) {
        for (var broadCastSessions : sessions.values()) {
            broadCastSessions.stream().filter(WebSocketSession::isOpen).forEach(session -> sendMessage(session,msg));
        }
    }

    private String getRoomId(WebSocketSession session) {
        var path = Objects.requireNonNull(session.getUri()).getPath(); // e.g. /ws/chat/room123
        var roomId =  path.substring(path.lastIndexOf("/") + 1);
        session.getAttributes().put("roomId", roomId);
        return roomId;
    }
}

package com.loop.troop.chat.application.chat;


import com.loop.troop.chat.application.persistance.repo.ChatRoomRepository;
import com.loop.troop.chat.application.persistance.repo.UserRepository;
import com.loop.troop.chat.domain.chat.ChatRoom;
import com.loop.troop.chat.domain.chat.GroupChatRoom;
import com.loop.troop.chat.domain.chat.SingleChatRoom;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.shared.dto.room.CreateRoomRequestDto;
import com.loop.troop.chat.shared.mapper.ChatRoomMapper;
import com.loop.troop.chat.shared.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomApplicationService {

    private final ChatRoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createRoom(CreateRoomRequestDto request) {

        // Fetch creator from DB
        var creatorEntity = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new IllegalArgumentException("Creator user not found"));
        User creator = UserMapper.toDomain(creatorEntity);

        ChatRoom room;

        if (RoomType.valueOf(request.getRoomType().toUpperCase()) == RoomType.SINGLE) {
            if (request.getOtherParticipantId() == null) {
                throw new IllegalArgumentException("Single chat requires second participant");
            }

            var otherEntity = userRepository.findById(request.getOtherParticipantId())
                    .orElseThrow(() -> new IllegalArgumentException("Second participant not found"));
            User other = UserMapper.toDomain(otherEntity);

            room = new SingleChatRoom(UUID.randomUUID().toString(),creator, other);

        } else {
            // Group chat
            List<User> participants = new ArrayList<>();
            if (request.getInitialParticipantIds() != null) {
                participants = request.getInitialParticipantIds().stream()
                        .map(id -> userRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id)))
                        .map(UserMapper::toDomain)
                        .toList();
            }

            room = new GroupChatRoom(UUID.randomUUID().toString(),creator, request.getGroupName(),
                    Boolean.TRUE.equals(request.getIsPermanent()), participants);
        }

        // Save
        roomRepository.save(ChatRoomMapper.toEntity(room));

        return room.getRoomId();
    }

    public ChatRoom getRoomById(String roomId){
        return ChatRoomMapper.toDomain(roomRepository.findById(roomId).orElse(null));
    }

    public List<ChatRoom> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(ChatRoomMapper::toDomain)
                .toList();
    }

    @Transactional
    public void addParticipant(String roomId, String userId) {
        var roomOpt = roomRepository.findById(roomId);
        var userOpt = userRepository.findById(userId);

        if (roomOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("Room or User not found");
        }

        var roomEntity = roomOpt.get();
        var userEntity = userOpt.get();

        // Avoid duplicates
        if (roomEntity.getParticipants().stream()
                .noneMatch(u -> u.getUserId().equals(userId))) {
            roomEntity.getParticipants().add(userEntity);
        }

        roomRepository.save(roomEntity);
    }

    @Transactional
    public void removeParticipant(String roomId, String userId) {
        var roomOpt = roomRepository.findById(roomId);
        roomOpt.ifPresent(roomEntity -> {
            roomEntity.getParticipants().removeIf(u -> u.getUserId().equals(userId));
            roomRepository.save(roomEntity);
        });
    }
}

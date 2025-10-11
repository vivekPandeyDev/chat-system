package com.loop.troop.chat.infrastructure.jpa.adapter;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.ChatRoomPersistence;
import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.User;
import com.loop.troop.chat.domain.exception.ChatRoomServiceException;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity;
import com.loop.troop.chat.infrastructure.jpa.repository.ChatRoomRepository;
import com.loop.troop.chat.infrastructure.jpa.repository.UserRepository;
import com.loop.troop.chat.infrastructure.shared.mapper.ChatRoomMapper;
import com.loop.troop.chat.infrastructure.shared.mapper.UserMapper;
import com.loop.troop.chat.infrastructure.shared.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.loop.troop.chat.infrastructure.shared.mapper.ChatRoomMapper.toDomain;
import static com.loop.troop.chat.infrastructure.shared.mapper.ChatRoomMapper.toEntity;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRoomJpaPersistenceAdapter implements ChatRoomPersistence {

	private final ChatRoomRepository chatRoomRepository;

	private final UserRepository userRepository;

	@Override
	public ChatRoom save(ChatRoom chatRoom) {
		ChatRoomEntity savedChatRoom = chatRoomRepository.save(toEntity(chatRoom));
		return toDomain(savedChatRoom);
	}

	@Override
	public Optional<ChatRoom> findById(String chatRoomId) {
		if (!Utility.isValidUUid(chatRoomId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		return chatRoomRepository.findById(UUID.fromString(chatRoomId)).map(ChatRoomMapper::toDomain);
	}

	@Override
	public PageResponse<ChatRoom> findAll(PaginationQuery paginationQuery) {
		// Set defaults if null
		var page = paginationQuery.page() != null ? paginationQuery.page() : 0;
		var size = paginationQuery.size() != null ? paginationQuery.size() : 10;
		String createdAt = "createdAt";
		var sortBy = paginationQuery.sortBy() != null ? paginationQuery.sortBy() : createdAt;
		var direction = "desc".equalsIgnoreCase(paginationQuery.sortDir()) ? Sort.Direction.DESC : Sort.Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
		log.info("Pageable data: {}", pageable);
		var entityPage = chatRoomRepository.findAll(pageable);

		var users = entityPage.getContent().stream().map(ChatRoomMapper::toDomain).toList();

		return new PageResponse<>(users, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(),
				entityPage.getTotalPages());
	}

	@Override
	public void addParticipants(String roomId, String userId) {
		if (!Utility.isValidUUid(roomId) || !Utility.isValidUUid(userId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		var chatRoomEntity = chatRoomRepository.findById(UUID.fromString(roomId))
			.orElseThrow(() -> ChatRoomServiceException.roomNotFound(roomId));
		var currentParticipant = userRepository.findById(UUID.fromString(userId))
			.orElseThrow(() -> UserServiceException.userNotFound(userId));
		if (!chatRoomEntity.getParticipants().contains(currentParticipant))
			chatRoomEntity.getParticipants().add(currentParticipant);
		chatRoomRepository.save(chatRoomEntity);
	}

	@Override
	public void removeParticipants(String roomId, String userId) {
		if (!Utility.isValidUUid(roomId) || !Utility.isValidUUid(userId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		var chatRoomEntity = chatRoomRepository.findById(UUID.fromString(roomId))
			.orElseThrow(() -> ChatRoomServiceException.roomNotFound(roomId));
		var currentParticipant = userRepository.findById(UUID.fromString(userId))
			.orElseThrow(() -> UserServiceException.userNotFound(userId));
		chatRoomEntity.getParticipants().remove(currentParticipant);
		chatRoomRepository.save(chatRoomEntity);
	}

	public List<User> getRoomParticipant(String chatRoomId) {
		if (!Utility.isValidUUid(chatRoomId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		return chatRoomRepository.findParticipantByRoomId(UUID.fromString(chatRoomId))
			.stream()
			.map(UserMapper::toDomain)
			.toList();
	}

}

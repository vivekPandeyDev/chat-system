package com.loop.troop.chat.infrastructure.jpa.adapter;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.MessagePersistence;
import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.infrastructure.jpa.repository.MessageRepository;
import com.loop.troop.chat.infrastructure.shared.mapper.MessageMapper;
import com.loop.troop.chat.infrastructure.shared.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.loop.troop.chat.infrastructure.shared.mapper.MessageMapper.toDomain;
import static com.loop.troop.chat.infrastructure.shared.mapper.MessageMapper.toEntity;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageJpaPersistenceAdapter implements MessagePersistence {

	private final MessageRepository messageRepository;

	@Override
	public Message save(Message message) {
		var savedMessageEntity = messageRepository.save(toEntity(message));
		return toDomain(savedMessageEntity);
	}

	@Override
	public Optional<Message> findById(String messageId) {
		if (!Utility.isValidUUid(messageId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		return messageRepository.findById(UUID.fromString(messageId)).map(MessageMapper::toDomain);
	}

	@Override
	public PageResponse<Message> findAllByRoomId(String roomId, PaginationQuery paginationQuery) {
		if (!Utility.isValidUUid(roomId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		// Set defaults if null
		var page = paginationQuery.page() != null ? paginationQuery.page() : 0;
		var size = paginationQuery.size() != null ? paginationQuery.size() : 10;
		String sentAt = "sentAt";
		var sortBy = paginationQuery.sortBy() != null ? paginationQuery.sortBy() : sentAt;
		var direction = "desc".equalsIgnoreCase(paginationQuery.sortDir()) ? Sort.Direction.DESC : Sort.Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
		log.info("Pageable data: {}", pageable);
		var entityList = messageRepository.findMessageByRoomId(UUID.fromString(roomId), pageable);

		var messages = entityList.getContent().stream().map(MessageMapper::toDomain).toList();

		return new PageResponse<>(messages, entityList.getNumber(), entityList.getSize(), entityList.getTotalElements(),
				entityList.getTotalPages());

	}

}

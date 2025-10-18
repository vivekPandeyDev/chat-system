package com.loop.troop.chat.infrastructure.jpa.adapter;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.UserPersistence;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.User;
import com.loop.troop.chat.infrastructure.jpa.repository.UserRepository;
import com.loop.troop.chat.infrastructure.shared.mapper.UserMapper;
import com.loop.troop.chat.infrastructure.shared.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.loop.troop.chat.infrastructure.shared.mapper.UserMapper.toDomain;
import static com.loop.troop.chat.infrastructure.shared.mapper.UserMapper.toEntity;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserJpaPersistenceAdapter implements UserPersistence {

	private final UserRepository userRepository;

	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("userId", "username", "email", "status");

	@Override
	public User save(User user) {
		var savedUserEntity = userRepository.save(toEntity(user));
		return toDomain(savedUserEntity);
	}

	@Override
	public Optional<User> findById(String userId) {
		if (!Utility.isValidUUid(userId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		return userRepository.findById(UUID.fromString(userId)).map(UserMapper::toDomain);
	}

	@Override
	public boolean existsByEmail(String email) {
		if (Utility.isBlank(email)) {
			throw new IllegalArgumentException("email is required to check user availability");
		}
		return userRepository.existsByEmail(email);
	}

	@Override
	public PageResponse<User> findAll(PaginationQuery paginationQuery) {
		// Set defaults if null
		var page = paginationQuery.page() != null ? paginationQuery.page() : 0;
		var size = paginationQuery.size() != null ? paginationQuery.size() : 10;
		String username = "username";
		var sortBy = paginationQuery.sortBy() != null ? paginationQuery.sortBy() : username;
		if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
			sortBy = username;
		}
		var direction = "desc".equalsIgnoreCase(paginationQuery.sortDir()) ? Sort.Direction.DESC : Sort.Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
		log.info("Pageable data: {}", pageable);
		var entityPage = userRepository.findAll(pageable);

		var users = entityPage.getContent().stream().map(UserMapper::toDomain).toList();

		return new PageResponse<>(users, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(),
				entityPage.getTotalPages());
	}

	@Override
	public void updateStatus(String userId, UserStatus status) {
		if (!Utility.isValidUUid(userId)) {
			throw new IllegalArgumentException("Invalid UUID format");
		}
		var savedUserEntity = userRepository.findById(UUID.fromString(userId))
			.orElseThrow(() -> UserServiceException.userNotFound(userId));
		savedUserEntity.setStatus(status);
		userRepository.save(savedUserEntity);
	}

	@Override
	public List<User> fetchUsersById(List<String> userIds) {
		for (var id : userIds) {
			if (!Utility.isValidUUid(id)) {
				throw new IllegalArgumentException("Invalid UUID format");
			}
		}
		var ids = userIds.stream().map(UUID::fromString).toList();
		return userRepository.findByUserIdIn(ids).stream().map(UserMapper::toDomain).toList();
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email).map(UserMapper::toDomain);
	}

}

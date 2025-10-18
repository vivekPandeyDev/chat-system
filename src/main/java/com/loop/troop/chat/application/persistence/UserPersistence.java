package com.loop.troop.chat.application.persistence;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserPersistence {

	User save(User user);

	Optional<User> findById(String userId);

	boolean existsByEmail(String email);

	PageResponse<User> findAll(PaginationQuery paginationQuery);

	void updateStatus(String userId, UserStatus status);

	List<User> fetchUsersById(List<String> userIds);

	Optional<User> findUserByEmail(String email);

}

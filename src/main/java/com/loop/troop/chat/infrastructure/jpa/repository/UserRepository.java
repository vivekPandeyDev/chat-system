package com.loop.troop.chat.infrastructure.jpa.repository;

import com.loop.troop.chat.infrastructure.jpa.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	boolean existsByEmail(String email);

	List<UserEntity> findByUserIdIn(Collection<UUID> userIds);

	Optional<UserEntity> findByEmail(String email);

	@Query("""
			    SELECT u
			    FROM UserEntity u
			    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
			       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))
			""")
	Page<UserEntity> searchUsers(@Param("query") String query, Pageable pageable);

}
package com.loop.troop.chat.infrastructure.jpa.repository;

import com.loop.troop.chat.infrastructure.jpa.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

	@Query("select m from MessageEntity m where m.room.roomId = ?1")
	Page<MessageEntity> findMessageByRoomId(UUID roomId, Pageable pageable);

}
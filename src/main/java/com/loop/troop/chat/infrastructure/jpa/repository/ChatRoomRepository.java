package com.loop.troop.chat.infrastructure.jpa.repository;

import com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity;
import com.loop.troop.chat.infrastructure.jpa.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, UUID> {

    @Query("select c.participants from ChatRoomEntity c where c.roomId = ?1")
    List<UserEntity> findParticipantByRoomId(UUID roomId);

    @Query("select c from ChatRoomEntity c inner join c.participants participants where participants.userId = ?1")
    Page<ChatRoomEntity> findByParticipantsByUserId(Pageable pageable, UUID userId);
}
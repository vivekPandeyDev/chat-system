package com.loop.troop.chat.infrastructure.jpa.repository;

import com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

}
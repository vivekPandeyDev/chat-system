package com.loop.troop.chat.persistance.repo;

import com.loop.troop.chat.persistance.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {}
package com.loop.troop.chat.adapter.persistance.repo;

import com.loop.troop.chat.adapter.persistance.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {}
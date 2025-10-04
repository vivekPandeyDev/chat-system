package com.loop.troop.chat.application.persistance.repo;

import com.loop.troop.chat.application.persistance.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

}
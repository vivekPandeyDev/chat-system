package com.loop.troop.chat.application.persistance.repo;

import com.loop.troop.chat.application.persistance.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

}
package com.loop.troop.chat.application.persistance.repo;
import com.loop.troop.chat.application.persistance.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, String> {
    @Query("select m from MessageEntity m where m.room.roomId = ?1")
    List<MessageEntity> findByRoomId(String roomId);

}
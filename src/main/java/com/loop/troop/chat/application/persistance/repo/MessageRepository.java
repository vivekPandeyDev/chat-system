package com.loop.troop.chat.application.persistance.repo;
import com.loop.troop.chat.application.persistance.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {

}
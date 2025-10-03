package com.loop.troop.chat.persistance.repo;
import com.loop.troop.chat.persistance.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {}
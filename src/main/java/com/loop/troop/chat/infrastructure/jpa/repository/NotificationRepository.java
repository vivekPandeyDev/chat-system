package com.loop.troop.chat.infrastructure.jpa.repository;

import com.loop.troop.chat.infrastructure.jpa.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

}
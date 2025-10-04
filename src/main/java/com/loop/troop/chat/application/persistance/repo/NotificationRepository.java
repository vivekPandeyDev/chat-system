package com.loop.troop.chat.application.persistance.repo;

import com.loop.troop.chat.application.persistance.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {

}
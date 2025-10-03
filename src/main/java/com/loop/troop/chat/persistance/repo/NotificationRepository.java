package com.loop.troop.chat.persistance.repo;

import com.loop.troop.chat.persistance.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {}
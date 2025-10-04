package com.loop.troop.chat.adapter.persistance.repo;

import com.loop.troop.chat.adapter.persistance.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {}
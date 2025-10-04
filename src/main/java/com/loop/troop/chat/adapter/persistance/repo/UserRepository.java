package com.loop.troop.chat.adapter.persistance.repo;

import com.loop.troop.chat.adapter.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {}
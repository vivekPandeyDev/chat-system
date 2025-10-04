package com.loop.troop.chat.application.persistance.repo;

import com.loop.troop.chat.application.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

}
package com.loop.troop.chat.shared.error.repo;

import com.loop.troop.chat.shared.error.entity.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {}
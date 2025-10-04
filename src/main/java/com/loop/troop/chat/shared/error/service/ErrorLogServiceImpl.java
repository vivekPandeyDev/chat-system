package com.loop.troop.chat.shared.error.service;

import com.loop.troop.chat.shared.error.entity.ErrorLog;
import com.loop.troop.chat.shared.error.repo.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ErrorLogServiceImpl implements ErrorLogService{
    private final ErrorLogRepository errorLogRepository;
    @Override
    public void persistError(String type, String code, String message, String detail, Exception ex, String path) {
        String stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        ErrorLog logEntry = new ErrorLog(type, code, message, detail, stackTrace, path, LocalDateTime.now());
        errorLogRepository.save(logEntry);
    }
}

package com.loop.troop.chat.infrastructure.shared.service;

import com.loop.troop.chat.infrastructure.jpa.entity.ErrorLog;
import com.loop.troop.chat.infrastructure.jpa.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ErrorLogServiceImpl implements ErrorLogService {

	private final ErrorLogRepository errorLogRepository;
    private static final Integer MAX_LENGTH = 8000;
    private static final String TRUNCATE_SUFFIX = " ...[truncated]";
	@Override
	public void persistError(String type, String code, String message, String detail, Exception ex, String path) {
		String stackTrace = Arrays.stream(ex.getStackTrace())
			.map(StackTraceElement::toString)
			.collect(Collectors.joining("\n"));
        // Truncate if necessary
        if (stackTrace.length() > MAX_LENGTH) {
            int allowedLength = MAX_LENGTH - TRUNCATE_SUFFIX.length();
            stackTrace = stackTrace.substring(0, allowedLength) + TRUNCATE_SUFFIX;
        }
		ErrorLog logEntry = new ErrorLog(type, code, message, detail, stackTrace, path, LocalDateTime.now());
		errorLogRepository.save(logEntry);
	}

}

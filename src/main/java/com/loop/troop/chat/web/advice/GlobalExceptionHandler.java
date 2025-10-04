package com.loop.troop.chat.web.advice;

import com.loop.troop.chat.web.entity.ErrorLog;
import com.loop.troop.chat.web.repo.ErrorLogRepository;
import com.loop.troop.chat.web.exception.ServiceException;
import com.loop.troop.chat.web.registry.builder.ServiceExceptionDetailBuilder;
import com.loop.troop.chat.web.registry.ServiceExceptionDetailRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String UNEXPECTED_ERROR = "Unexpected error";
    private final ServiceExceptionDetailRegistry detailRegistry;
    private final ErrorLogRepository errorLogRepository;

    public GlobalExceptionHandler(ServiceExceptionDetailRegistry detailRegistry, ErrorLogRepository errorLogRepository) {
        this.detailRegistry = detailRegistry;
        this.errorLogRepository = errorLogRepository;
    }

    // Service Exceptions
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ProblemDetail> handleServiceException(ServiceException ex, HttpServletRequest request) {
        log.error("ServiceException: {}", ex.getErrorCode(), ex);

        String detail = buildDetail(ex);
        persistError("SERVICE", ex.getErrorCode(), ex.getUserMessage(), detail, ex, request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatus(ex.getStatus());
        problem.setType(URI.create("https://example.com/probs/" + ex.getErrorCode().toLowerCase()));
        problem.setTitle(ex.getUserMessage());
        problem.setDetail(detail);
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(ex.getStatus()).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/INVALID_ARGUMENT"));
        problem.setTitle("Invalid Argument");
        problem.setProperty("errorCode", "INVALID_ARGUMENT");
        return problem;
    }

    // Handle Bean Validation errors (@NotBlank, @NotNull)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());

        String errors = ex.getBindingResult()
                          .getFieldErrors()
                          .stream()
                          .map(err -> err.getField() + ": " + err.getDefaultMessage())
                          .collect(Collectors.joining("; "));

        persistError("VALIDATION", "VALIDATION_ERROR", "Validation failed", errors, ex, request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create("https://example.com/probs/validation-error"));
        problem.setTitle("Validation failed");
        problem.setDetail(errors);
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.badRequest().body(problem);
    }

    // Handle ConstraintViolationException (@Validated on @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst()
                .orElse("Constraint violation");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problem.setType(URI.create("https://example.com/errors/CONSTRAINT_VIOLATION"));
        problem.setTitle("Constraint Violation");
        problem.setProperty("errorCode", "CONSTRAINT_VIOLATION");
        return problem;
    }

    // Unexpected Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleOtherExceptions(Exception ex, HttpServletRequest request) {
        log.error(UNEXPECTED_ERROR, ex);

        String detail = ex.getMessage() != null ? ex.getMessage() : "No additional details";
        persistError("UNEXPECTED", "UNEXPECTED_ERROR", UNEXPECTED_ERROR, detail, ex, request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(URI.create("https://example.com/probs/unexpected-error"));
        problem.setTitle(UNEXPECTED_ERROR);
        problem.setDetail("Something went wrong. Please contact support.");
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @SuppressWarnings("unchecked")
    private String buildDetail(ServiceException ex) {
        ServiceExceptionDetailBuilder<? extends ServiceException> builder = detailRegistry.getBuilder(ex.getClass());
        if (builder == null) {
            return "No additional details available";
        }
        return ((ServiceExceptionDetailBuilder<ServiceException>) builder).buildDetail(ex);
    }

    private void persistError(String type, String code, String message, String detail, Exception ex, String path) {
        String stackTrace = Arrays.stream(ex.getStackTrace())
                                  .map(StackTraceElement::toString)
                                  .collect(Collectors.joining("\n"));
        ErrorLog logEntry = new ErrorLog(type, code, message, detail, stackTrace, path, LocalDateTime.now());
        errorLogRepository.save(logEntry);
    }
}

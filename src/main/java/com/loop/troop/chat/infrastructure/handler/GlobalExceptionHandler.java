package com.loop.troop.chat.infrastructure.handler;

import com.loop.troop.chat.domain.exception.ServiceException;
import com.loop.troop.chat.domain.exception.builder.ServiceExceptionDetailBuilder;
import com.loop.troop.chat.infrastructure.shared.registry.ServiceExceptionDetailRegistry;
import com.loop.troop.chat.infrastructure.shared.service.ErrorLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

	public static final String UNEXPECTED_ERROR = "Unexpected error";

	public static final String ERROR_CODE = "errorCode";

	private final ServiceExceptionDetailRegistry detailRegistry;

	private final ErrorLogService errorLogService;

	// Service Exceptions
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ProblemDetail> handleServiceException(ServiceException ex, HttpServletRequest request) {
		log.error("ServiceException: message={}, status={}, code={}", ex.getUserMessage(), ex.getStatus(),
				ex.getErrorCode(), ex);
		String detail = buildDetail(ex);
		errorLogService.persistError("SERVICE", ex.getErrorCode(), ex.getUserMessage(), detail, ex,
				request.getRequestURI());
		ProblemDetail problem = ProblemDetail.forStatus(ex.getStatus());
		problem.setType(URI.create("https://example.com/probs/" + ex.getErrorCode().toLowerCase()));
		problem.setTitle(ex.getErrorCode());
		problem.setDetail(detail);
		problem.setInstance(URI.create(request.getRequestURI()));
		return ResponseEntity.status(ex.getStatus()).body(problem);
	}

	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ProblemDetail handleInvalidDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
		log.error("Data access error: {}", ex.getMessage(), ex);

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				ex.getMessage() != null ? ex.getMessage() : "Invalid data access usage");
		problem.setType(URI.create("https://example.com/errors/INVALID_DATA_ACCESS"));
		problem.setTitle("Invalid Data Access");
		problem.setProperty(ERROR_CODE, "INVALID_DATA_ACCESS");

		return problem;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
		log.warn("Illegal argument: {}", ex.getMessage());
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problem.setType(URI.create("https://example.com/errors/INVALID_ARGUMENT"));
		problem.setTitle("Invalid Argument");
		problem.setProperty(ERROR_CODE, "INVALID_ARGUMENT");
		return problem;
	}

	// Handle Bean Validation errors (@NotBlank, @NotNull)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		log.warn("Validation failed: {}", ex.getMessage());

		String errors = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(err -> err.getField() + ": " + err.getDefaultMessage())
			.collect(Collectors.joining("; "));
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setType(URI.create("https://example.com/probs/validation-error"));
		problem.setTitle("VALIDATION_ERROR");
		problem.setDetail(errors);
		problem.setInstance(URI.create(request.getRequestURI()));

		return ResponseEntity.badRequest().body(problem);
	}

	// Handle ConstraintViolationException (@Validated on @RequestParam)
	@ExceptionHandler(ConstraintViolationException.class)
	public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
		String errorMessage = ex.getConstraintViolations()
			.stream()
			.map(ConstraintViolation::getMessage)
			.findFirst()
			.orElse("Constraint violation");

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
		problem.setType(URI.create("https://example.com/errors/CONSTRAINT_VIOLATION"));
		problem.setTitle("Constraint Violation");
		problem.setProperty(ERROR_CODE, "CONSTRAINT_VIOLATION");
		return problem;
	}

	// Unexpected Exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleOtherExceptions(Exception ex, HttpServletRequest request) {
		log.error(UNEXPECTED_ERROR, ex);

		String detail = ex.getMessage() != null ? ex.getMessage() : "No additional details";
		errorLogService.persistError("UNEXPECTED", "UNEXPECTED_ERROR", UNEXPECTED_ERROR, detail, ex,
				request.getRequestURI());
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

}

package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.usecase.UserUseCase;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.user.GenerateTokenRequest;
import com.loop.troop.chat.infrastructure.shared.dto.user.JwtResponse;
import com.loop.troop.chat.infrastructure.shared.dto.user.UserResponseDto;
import com.loop.troop.chat.infrastructure.shared.mapper.UserMapper;
import com.loop.troop.chat.infrastructure.web.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
@Slf4j
public class TokenController {

	private final JwtConfig jwtConfig;

	private final UserUseCase userUseCase;

	@PostMapping("/generate")
	public ResponseEntity<ApiResponse<JwtResponse>> generateToken(@Valid @RequestBody GenerateTokenRequest request) {
		var email = request.email();
		var user = userUseCase.fetchUserByEmail(email)
			.orElseThrow(() -> UserServiceException.userNotFoundWithEmail(email));
		Key key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
		Instant now = Instant.now();
		Instant expiry = now.plus(jwtConfig.getExpiryMinutes(), ChronoUnit.MINUTES);
		String token = Jwts.builder()
			.subject(email)
			.issuer(jwtConfig.getIssuer())
			.claim("userId", user.getUserId())
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiry))
			.signWith(key)
			.compact();

		var response = new JwtResponse(token, now.getEpochSecond(), expiry.getEpochSecond(),
				jwtConfig.getExpiryMinutes() * 60L, jwtConfig.getIssuer(), email);
		return ResponseEntity.ok(new ApiResponse<>(true, "Token generated successfully", response));

	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser(
			@RequestHeader("Authorization") String authorizationHeader) {

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
		}
		var token = authorizationHeader.substring(7);
		var userId = getUserIdFromToken(token);
		var user = userUseCase.fetchUserByUserId(userId).orElseThrow(() -> UserServiceException.userNotFound(userId));
		var userFetchResponse = new ApiResponse<>(true, "User fetched successfully",
				UserMapper.toResponseDto(user, userUseCase.fetchProfileUrl(user)));
		return ResponseEntity.ok(userFetchResponse);

	}

	private String getUserIdFromToken(String token) {

		var key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
		try {
			var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
			return claims.get("userId", String.class);
		}
		catch (Exception e) {
			log.error("Error parsing claims: {}", e.getMessage(), e);
		}

		return null;
	}

}

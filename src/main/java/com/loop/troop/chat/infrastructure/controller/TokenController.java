package com.loop.troop.chat.infrastructure.controller;

import com.loop.troop.chat.application.usecase.UserUseCase;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.infrastructure.shared.dto.ApiResponse;
import com.loop.troop.chat.infrastructure.shared.dto.user.GenerateTokenRequest;
import com.loop.troop.chat.infrastructure.shared.dto.user.JwtResponse;
import com.loop.troop.chat.infrastructure.web.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtConfig jwtConfig;
    private final UserUseCase userUseCase;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<JwtResponse>> generateToken(@Valid @RequestBody GenerateTokenRequest request) {
        var email = request.email();
        userUseCase.fetchUserByEmail(email).orElseThrow(() -> UserServiceException.userNotFoundWithEmail(email));
        Key key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtConfig.getExpiryMinutes(), ChronoUnit.MINUTES);
        String token = Jwts.builder()
                .subject(email)
                .issuer(jwtConfig.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();

        var response = new JwtResponse(token, now.getEpochSecond(), expiry.getEpochSecond(),
                jwtConfig.getExpiryMinutes() * 60L, jwtConfig.getIssuer(), email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Token generated successfully", response));

    }

}

package com.loop.troop.chat.infrastructure.shared.dto.user;

public record JwtResponse(String token, long issuedAt, long expiresAt, long expiresIn, String issuer, String username) {
}
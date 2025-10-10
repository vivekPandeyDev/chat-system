package com.loop.troop.chat.infrastructure.shared.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loop.troop.chat.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private String userId;
    private String username;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatarUrl;
    private UserStatus status;
}

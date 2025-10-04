package com.loop.troop.chat.application.persistance.entity;

import com.loop.troop.chat.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    private String userId;

    private String username;
    private String email;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}

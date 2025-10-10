package com.loop.troop.chat.infrastructure.jpa.entity;

import com.loop.troop.chat.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String username;
    private String email;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}

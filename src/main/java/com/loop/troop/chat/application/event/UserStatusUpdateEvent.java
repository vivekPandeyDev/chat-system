package com.loop.troop.chat.application.event;

import com.loop.troop.chat.domain.user.User;

public record UserStatusUpdateEvent(User user) {
}


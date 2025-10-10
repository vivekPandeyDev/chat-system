package com.loop.troop.chat.application.event;

import com.loop.troop.chat.domain.User;

public record UserRegisteredEvent(User user) {
}

package com.loop.troop.chat.domain.observer;

public interface DomainObserver<E> {
    void onEvent(E event);
}

package com.loop.troop.chat.shared.registry.builder;

import com.loop.troop.chat.shared.exception.ServiceException;

public interface ServiceExceptionDetailBuilder<T extends ServiceException> {
    String buildDetail(T exception);
}

package com.loop.troop.chat.web.registry.builder;

import com.loop.troop.chat.web.exception.ServiceException;

public interface ServiceExceptionDetailBuilder<T extends ServiceException> {
    String buildDetail(T exception);
}

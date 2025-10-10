package com.loop.troop.chat.domain.exception.builder;

import com.loop.troop.chat.domain.exception.ServiceException;

public interface ServiceExceptionDetailBuilder<T extends ServiceException> {
    String buildDetail(T exception);
}

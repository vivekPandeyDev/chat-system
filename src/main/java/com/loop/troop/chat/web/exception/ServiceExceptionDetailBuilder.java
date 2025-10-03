package com.loop.troop.chat.web.exception;

public interface ServiceExceptionDetailBuilder<T extends ServiceException> {
    String buildDetail(T exception);
}

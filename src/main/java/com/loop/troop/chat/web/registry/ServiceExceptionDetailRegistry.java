package com.loop.troop.chat.web.registry;

import com.loop.troop.chat.web.exception.ServiceException;
import com.loop.troop.chat.web.registry.builder.ServiceExceptionDetailBuilder;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceExceptionDetailRegistry {

    private final Map<Class<? extends ServiceException>, ServiceExceptionDetailBuilder<?>> registry = new HashMap<>();

    @SuppressWarnings("unchecked")
    public ServiceExceptionDetailRegistry(List<ServiceExceptionDetailBuilder<?>> builders) {
        // Spring injects all beans implementing ServiceExceptionDetailBuilder
        builders.forEach(builder -> {
            Class<?> type = GenericTypeResolver.resolveTypeArgument(builder.getClass(), ServiceExceptionDetailBuilder.class);
            if (type != null && ServiceException.class.isAssignableFrom(type)) {
                registry.put((Class<? extends ServiceException>) type, builder);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends ServiceException> ServiceExceptionDetailBuilder<T> getBuilder(Class<T> exceptionClass) {
        return (ServiceExceptionDetailBuilder<T>) registry.get(exceptionClass);
    }
}

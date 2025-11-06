// ...new file...
package com.loop.troop.chat.infrastructure.jpa.filter;

import com.loop.troop.chat.infrastructure.jpa.audit.CurrentUserHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Simple filter that reads X-User-Id header and stores it in a ThreadLocal for auditing.
 * If you have a security context, replace this with a principal-based lookup.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CurrentUserFilter extends HttpFilter {

    private static final String HEADER = "X-User-Id";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            String user = req.getHeader(HEADER);
            if (user != null && !user.isBlank()) {
                CurrentUserHolder.setCurrentUser(user);
            }
            chain.doFilter(req, res);
        } finally {
            CurrentUserHolder.clear();
        }
    }
}


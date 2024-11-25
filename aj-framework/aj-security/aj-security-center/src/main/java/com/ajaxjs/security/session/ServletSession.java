package com.ajaxjs.security.session;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Simple session implemented by the Default servlet session.
 */
public class ServletSession implements ISessionService {
    static HttpSession getSession() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest().getSession();
    }

    @Override
    public String get(String key) {
        Object v = getSession().getAttribute(key);
        return v == null ? null : v.toString();
    }

    @Override
    public void set(String key, String value) {
        getSession().setAttribute(key, value);
    }

    @Override
    public void delete(String key) {
        getSession().removeAttribute(key);
    }
}

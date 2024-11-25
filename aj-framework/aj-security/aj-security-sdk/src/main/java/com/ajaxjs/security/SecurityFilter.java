package com.ajaxjs.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SecurityFilter {
    default void onRequest(HttpServletRequest req) {
        throw new SecurityException();
    }

    default void onRequest(HttpServletRequest req, HttpServletResponse resp) {
        throw new SecurityException();
    }
}

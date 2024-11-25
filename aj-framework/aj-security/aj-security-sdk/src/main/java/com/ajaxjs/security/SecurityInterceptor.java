package com.ajaxjs.security;

import com.ajaxjs.Version;
import com.ajaxjs.security.referer.HttpReferer;
import com.ajaxjs.security.referer.HttpRefererCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class SecurityInterceptor implements HandlerInterceptor {
    @Autowired(required = false)
    HttpReferer httpReferer;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (Version.isDebug)
            return true;
        else if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (httpReferer != null && method.getAnnotation(HttpRefererCheck.class) != null) {
                httpReferer.onRequest(req);
            }
        }

        return true;
    }
}

package com.ajaxjs.api.time_signature;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class TimeSignatureInterceptor implements HandlerInterceptor {
    private TimeSignature signatureService;

    /**
     * Whether is global access check
     */
    private boolean isGlobalCheck;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isGlobalCheck || methodCheck(handler)) {

            String signature = request.getParameter("tsign");// 获取签名参数

            if (!StringUtils.hasText(signature))
                throw new IllegalArgumentException("Missing Parameters: tsign.");

            if (signatureService.verifySignature(signature))
                throw new SecurityException("Invalid or expired signature.");
        }

        return true;
    }

    private static boolean methodCheck(Object handler) {
        if (!(handler instanceof HandlerMethod))
            return true;
        else {
            HandlerMethod h = (HandlerMethod) handler;

            return h.getMethodAnnotation(TimeSignatureVerify.class) != null;
        }
    }
}

package com.ajaxjs.api.limit;

import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流
 *
 * @author devcxl
 */
public class LimitInterceptor implements HandlerInterceptor {
    /**
     * redisKey模板
     */
    private static final String LIMIT_KEY_TEMPLATE = "limit_%s_%s";

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * 检查API限制
     *
     * @param limit    注解
     * @param limitKey RedisKey
     * @return 是否拦截
     */
    private boolean checkLimit(LimitAccess limit, String limitKey) {
        int max = limit.max();
        int time = limit.time();
        TimeUnit timeUnit = limit.timeUnit();
        Integer count = redisTemplate.opsForValue().get(limitKey);

        if (count != null) {
            if (count < max) {
                Long expire = redisTemplate.getExpire(limitKey);

                if (expire != null && expire <= 0)
                    redisTemplate.opsForValue().set(limitKey, 1, time, timeUnit);
                else
                    redisTemplate.opsForValue().set(limitKey, ++count, time, timeUnit);
            } else
                // RuntimeException 可以改为自定义异常 可在全局异常捕获中处理并返回给前端
                throw new RuntimeException(limit.msg());
        } else
            redisTemplate.opsForValue().set(limitKey, 1, time, timeUnit);

        return true;
    }

    /**
     * default preHandle
     *
     * @param request  请求
     * @param response 返回
     * @param handler  请求方法
     * @return 是否拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        LimitAccess limit = method.getAnnotation(LimitAccess.class);

        if (limit == null)
            return true;

        // 将请求路径和ip地址设为唯一标识 也可以自定义
        String limitKey = String.format(LIMIT_KEY_TEMPLATE, request.getRequestURI(), ServletUtil.getClientIP(request));

        boolean checkLimit = checkLimit(limit, limitKey);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 返回 429 错误码

        try {
            response.getWriter().write("Too many requests"); // 返回错误消息
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return checkLimit;
    }
}
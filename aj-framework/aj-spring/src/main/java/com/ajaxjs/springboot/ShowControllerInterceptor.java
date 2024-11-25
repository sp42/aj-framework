package com.ajaxjs.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 获得 Controller 方法名、请求参数和注解信息
 *
 */
@Slf4j
public class ShowControllerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod) handler;
            log.info("请求 URL：{} 对应的控制器方法：{}", request.getRequestURL(), h);

            StringBuffer s = new StringBuffer();
            Map<String, String[]> parameterMap = request.getParameterMap();

            if (!parameterMap.isEmpty()) {
                for (String key : parameterMap.keySet())
                    s.append(key).append("=").append(Arrays.toString(parameterMap.get(key))).append("\n");

                log.info("{} 请求参数：\n{}", request.getMethod(), s);
            }
        }

        return true;
    }
}
package com.ajaxjs.springboot;

import com.ajaxjs.springboot.annotation.JsonMessage;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

public abstract class GlobalResponseResult implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        System.out.println("supports:" + returnType);
        return true;
    }

    private static final String OK = "操作成功";

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResponseResultWrapper responseResult = new ResponseResultWrapper();
        responseResult.setStatus(1);

        JsonMessage annotation = returnType.getMethod().getAnnotation(JsonMessage.class);

        if (annotation != null)
            responseResult.setMessage(annotation.value());
         else
            responseResult.setMessage(OK);

        responseResult.setData(body);

        return responseResult;
    }
}

package com.ajaxjs.springboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)

/**
 * 返回 JSON 结构时候，自定义 message 内容
 */
@Target({ElementType.METHOD})
public @interface JsonMessage {
    /**
     * message 内容，必填
     *
     * @return message 内容
     */
    String value();
}

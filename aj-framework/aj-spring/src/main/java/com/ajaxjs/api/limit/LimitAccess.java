package com.ajaxjs.api.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitAccess {
    /**
     * 次数
     *
     * @return 次数
     */
    int max() default 2;

    /**
     * 时间
     *
     * @return 时间
     */
    int time() default 5;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 提示
     *
     * @return 提示
     */
    String msg() default "系统繁忙，请稍后再试。";
}

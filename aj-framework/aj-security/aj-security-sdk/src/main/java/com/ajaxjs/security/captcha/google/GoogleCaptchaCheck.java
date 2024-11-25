package com.ajaxjs.security.captcha.google;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止 Captcha
 *
 * @author Frank Cheung<sp42@qq.com>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoogleCaptchaCheck {
}

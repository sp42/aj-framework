package com.ajaxjs.security.xxs;

public class XssException extends SecurityException {
    public XssException(String msg) {
        super(msg);
    }
}

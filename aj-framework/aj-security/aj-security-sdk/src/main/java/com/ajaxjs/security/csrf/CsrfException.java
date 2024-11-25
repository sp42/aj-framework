package com.ajaxjs.security.csrf;

public class CsrfException extends SecurityException {
    public CsrfException(String msg) {
        super(msg);
    }
}

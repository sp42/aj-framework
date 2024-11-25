package com.ajaxjs.security.repeat_req;

public class RepeatRequestException extends SecurityException {
    public RepeatRequestException(String msg) {
        super(msg);
    }
}

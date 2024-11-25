package com.ajaxjs.util.http_request;

/**
 * HTTP Request Exception
 */
public class RequestException extends RuntimeException {
    public RequestException() {
    }

    public RequestException(String msg) {
        super(msg);
    }
}

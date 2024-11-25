package com.ajaxjs.springboot;

import lombok.Data;

@Data
public class ResponseResultWrapper {
    private Integer status;

    private Integer total;

    private String errorCode;

    private String message;

    private Object data;
}

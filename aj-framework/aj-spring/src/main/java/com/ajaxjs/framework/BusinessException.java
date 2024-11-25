package com.ajaxjs.framework;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 自定义的业务异常
 */
@Data
public class BusinessException extends RuntimeException {
    public static final long serialVersionUID = -6735897190745766930L;
    /**
     * 自定义的错误代码
     */
    private String errCode;

    /**
     * 创建一个业务异常
     */
    public BusinessException() {
    }

    /**
     * 创建一个业务异常
     *
     * @param msg 业务异常的信息
     */
    public BusinessException(String msg) {
        super(msg);
        this.errCode = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}

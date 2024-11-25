package com.ajaxjs.springboot;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * 统一返回结构
 */
@Deprecated
public class GlobalResponseResultDep extends AbstractHttpMessageConverter<Object> {
    /**
     * 对于 POST Raw Body 的识别，通常是 JSON
     */
    private static final MediaType CONTENT_TYPE = new MediaType("application", "json");

    /**
     * 对于 POST 标准表单的识别
     */
    private static final MediaType CONTENT_TYPE_FORM = new MediaType("application", "x-www-form-urlencoded");

    /**
     *
     */
    private static final MediaType CONTENT_TYPE_FORM2 = new MediaType("multipart", "form-data");

    public GlobalResponseResultDep() {
        super(CONTENT_TYPE, CONTENT_TYPE_FORM, CONTENT_TYPE_FORM2);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
//        System.out.println("s-------" + clazz);
        return true;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        System.out.println("readInternal-------" + clazz);
        return null;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws HttpMessageNotWritableException {
        System.out.println(o);
    }
}

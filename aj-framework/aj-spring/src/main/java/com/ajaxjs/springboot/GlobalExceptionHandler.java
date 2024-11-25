package com.ajaxjs.springboot;

import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse resp, Object handler, Exception ex) {
        log.warn("ERROR>>", ex);

        Throwable _ex = ex.getCause() != null ? ex.getCause() : ex;
        String msg = _ex.getMessage();

        if (msg == null)
            msg = _ex.toString();

        ResponseResultWrapper resultWrapper = new ResponseResultWrapper();
        resultWrapper.setStatus(0);

        if (_ex instanceof BusinessException) {
            BusinessException b = (BusinessException) _ex;
            resultWrapper.setErrorCode(StringUtils.hasText(b.getErrCode()) ? b.getErrCode() : "500");
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        else if (_ex instanceof ICustomException) {
//            int errCode = ((ICustomException) _ex).getErrCode();
//            resultWrapper.setErrorCode(String.valueOf(errCode));
//            resp.setStatus(errCode);
        } else if (_ex instanceof SecurityException || _ex instanceof IllegalAccessError || _ex instanceof IllegalAccessException) {// 设置状态码
            resultWrapper.setErrorCode("403");
            resp.setStatus(HttpStatus.FORBIDDEN.value());
        } else {
            resultWrapper.setErrorCode("500");
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        resultWrapper.setMessage(javaValue2jsonValue(msg));

        resp.setCharacterEncoding(StrUtil.UTF8_SYMBOL); // 避免乱码
        resp.setHeader("Cache-Control", "no-cache, must-revalidate");
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置 ContentType

        try {
            resp.getWriter().write(JsonUtil.toJson(resultWrapper));
        } catch (IOException e) {
            log.warn("ERROR>>", e);
        }

        return new ModelAndView();
    }

    /**
     * 转义注释和缩进
     *
     * @param str JSON 字符串
     * @return 转换后的字符串
     */
    private static String javaValue2jsonValue(String str) {
        return str.replaceAll("\"", "\\\\\"").replaceAll("\t", "\\\\\t");
    }
}

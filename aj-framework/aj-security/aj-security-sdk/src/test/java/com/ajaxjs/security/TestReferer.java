package com.ajaxjs.security;

import com.ajaxjs.security.referer.HttpReferer;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestReferer {
    @Test
    public void test() {
        // 模拟 HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn("http://foo.com");

        HttpReferer httpReferer = new HttpReferer();
        httpReferer.setALLOWED_REFERRERS(List.of("http://foo.com"));
        httpReferer.onRequest(request);
    }
}

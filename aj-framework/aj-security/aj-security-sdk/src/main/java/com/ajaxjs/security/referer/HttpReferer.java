package com.ajaxjs.security.referer;

import com.ajaxjs.security.SecurityFilter;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Simply check the HTTP Referer
 */
@Data
public class HttpReferer implements SecurityFilter {
    /**
     * Add your domains here to open
     */
    protected List<String> ALLOWED_REFERRERS;

    @Override
    public void onRequest(HttpServletRequest req) {
        var referer = req.getHeader("Referer");  // 获取 Referer 头

        if (!StringUtils.hasText(referer) || !ALLOWED_REFERRERS.contains(referer))
            throw new HttpRefererException("Invalid Referer header.");
    }
}

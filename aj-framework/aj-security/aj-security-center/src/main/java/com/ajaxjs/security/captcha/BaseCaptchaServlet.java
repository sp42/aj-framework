package com.ajaxjs.security.captcha;

import com.ajaxjs.security.captcha.image.ICaptchaImageProvider;
import com.ajaxjs.util.StrUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class CaptchaServlet
 */
public abstract class BaseCaptchaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected String sessionKeyValue = "CAPTCHA";
    protected ICaptchaImageProvider provider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set to expire far in the past.
        resp.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // 0Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");

        // return a jpeg
        resp.setContentType("image/jpeg");

        // create the text for the image
        String capText = StrUtil.getRandomString(6);
        // store the text in the session
        req.getSession().setAttribute(this.sessionKeyValue, capText);

        ImageIO.write(provider.getRenderedImage(200, 50, capText), "jpg", resp.getOutputStream());
    }
}
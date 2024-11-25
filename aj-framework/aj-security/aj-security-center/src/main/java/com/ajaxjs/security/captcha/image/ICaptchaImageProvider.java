package com.ajaxjs.security.captcha.image;

import java.awt.image.RenderedImage;

/**
 * A Provider for image of captcha
 */
public interface ICaptchaImageProvider {

    /**
     * Get rendered imageF
     *
     * @param width
     * @param height
     * @param randomStr
     * @return
     */
    default RenderedImage getRenderedImage(int width, int height, String randomStr) {
        throw new NullPointerException();
    }
}

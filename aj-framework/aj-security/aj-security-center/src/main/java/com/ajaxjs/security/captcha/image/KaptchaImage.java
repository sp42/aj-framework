package com.ajaxjs.security.captcha.image;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import java.awt.image.RenderedImage;
import java.util.Properties;

public class KaptchaImage implements ICaptchaImageProvider {
    @Override
    public RenderedImage getRenderedImage(int width, int height, String randomStr) {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.border.color", "34,114,200");
        properties.setProperty("kaptcha.image.width", String.valueOf(width)); // 200
        properties.setProperty("kaptcha.image.height", String.valueOf(height)); // 50
        //properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Arial Narrow,Serif,Helvetica,Tahoma,Times New Roman,Verdana");
        properties.setProperty("kaptcha.textproducer.font.size", "38");

        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        // 生成验证码
//        String captcha = defaultKaptcha.createText();
        // 生成图片验证码
        return defaultKaptcha.createImage(randomStr);
    }
}

package com.ajaxjs.captcha;

import org.junit.Test;
import com.ajaxjs.security.captcha.image.SimpleCaptchaImage;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class TestImage {
    @Test
    public void testImageRenderer() throws IOException {
        saveImg(new SimpleCaptchaImage().getRenderedImage(200, 50, "343d"));
    }

    static void saveImg(RenderedImage renderedImage) throws IOException {
        boolean result = ImageIO.write(renderedImage, "jpg",
                new File("c://temp//test.jpg"));

        if (!result)
            throw new Error("Unsupported file format");

        System.out.println("Image saved successfully ");
    }

    @Test
    public void testK() throws IOException {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.border.color", "34,114,200");
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "50");
        //properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Arial Narrow,Serif,Helvetica,Tahoma,Times New Roman,Verdana");
        properties.setProperty("kaptcha.textproducer.font.size", "38");

        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        // 生成验证码
        String captcha = defaultKaptcha.createText();
        // 生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(captcha);
        saveImg(image);
    }

    @Test
    public void test2() {
        var d = 9;
        System.out.println(d);
        var str = "foo";
        str.trim();
        str.strip();
        str.isBlank();
        CharSequence d9;
    }
}

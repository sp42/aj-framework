package com.ajaxjs.security.captcha.image;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Default image provider, just using Java AWT to renderer image
 */
public class SimpleCaptchaImage implements ICaptchaImageProvider {
    @Override
    public RenderedImage getRenderedImage(int width, int height, String randomStr) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);// 在内存中创建图像
        Graphics g = image.getGraphics(); // 获取图形上下文
        g.setColor(getRandColor(200, 250)); // 设定背景
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 30)); // 设定字体
        g.setColor(getRandColor(160, 200));

        Random random = new Random();// 随机产生干扰线
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width), y = random.nextInt(height);
            int xl = random.nextInt(12), yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String[] arr = randomStr.split("");
        for (int i = 0; i < 4; i++) {
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))); // 将认证码显示到图象中
            g.drawString(arr[i], 13 * i + 6, 16);// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
        }

        g.dispose();// 图象生效

        return image;
    }

    /**
     * 生成随机颜色
     *
     * @param fc
     * @param bc
     * @return 随机颜色
     */
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;

        Random random = new Random();
        int r = fc + random.nextInt(bc - fc), g = fc + random.nextInt(bc - fc), b = fc + random.nextInt(bc - fc);

        return new Color(r, g, b);
    }

    public static void main(String[] args) throws IOException {
        boolean result = ImageIO.write(new SimpleCaptchaImage().getRenderedImage(200, 50, "343d"), "jpg",
                new File("/Users/xinzhang/code/code/temp/test.jpg"));

        if (!result)
            throw new Error("Unsupported file format");

        System.out.println("Image saved successfully ");
    }
}

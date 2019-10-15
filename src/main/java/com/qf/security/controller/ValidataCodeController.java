package com.qf.security.controller;

import com.qf.security.validate.ImageCode;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
@RequestMapping("/validate/code")
public class ValidataCodeController {

    // Session的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    public static final String VALIDATE_CODE_KEY = "VALIDATE_CODE_KEY";

    @RequestMapping
    public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ImageCode imageCode = generate(); //生成验证码

        // 将ImageCode存入到session
        sessionStrategy.setAttribute(new ServletWebRequest(request), VALIDATE_CODE_KEY, imageCode);

        //将图片写入前端
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    //生成图片验证码
    private ImageCode generate() {
        int width = 67;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 随机生成文字
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 22);
        }

        g.dispose();

        return new ImageCode(sRand, image, 60);
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}

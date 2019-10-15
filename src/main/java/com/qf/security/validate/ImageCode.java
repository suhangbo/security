package com.qf.security.validate;

import org.apache.tomcat.jni.Local;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class ImageCode extends ValidateCode{

    private BufferedImage image;

    public ImageCode(){}

    public ImageCode(String code, BufferedImage image, int num) {
        super(code, num);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

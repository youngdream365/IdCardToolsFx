package com.youngdream.idcardtools.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class ImgUtil {
    private static final Logger LOGGER = Logger.getLogger(ImgUtil.class.getName());

    public static final Image WINDOW_ICON = loadImage("/img/icon/icon-taskbar.png");
    public static final Image SYS_BAR_ICON = loadImage("/img/icon/icon-header.png");
    public static final Image PIN_ICON = loadImage("/img/header/pin.png");
    public static final Image UNPIN_ICON = loadImage("/img/header/unpin.png");
    public static final Image CLOSE_ICON = loadImage("/img/header/close.png");
    public static final Image MINIMIZE_ICON = loadImage("/img/header/minimize.png");

    private ImgUtil() {
    }

    public static Image loadImage(String path) {
        Image image = null;
        try {
            image = new Image(ImgUtil.class.getResourceAsStream(path));
        } catch (Exception e) {
            LOGGER.warning("Error loading image: " + path);
        }
        return image;
    }

    /**
     * AWT Image 转 javaFX Image
     */
    public static Image convertAWTImage2FXImage(java.awt.Image awtImage) {
        if (awtImage instanceof BufferedImage) {
            return SwingFXUtils.toFXImage((BufferedImage)awtImage, null);
        } else {
            BufferedImage bufferedImage = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null), 2);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(awtImage, 0, 0, null);
            g.dispose();
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
    }

    /**
     * javaFX Image 转 AWT Image
     */
    public static java.awt.Image convertFXImage2AWTImage(Image fxImage) {
        return SwingFXUtils.fromFXImage(fxImage, null);
    }
}

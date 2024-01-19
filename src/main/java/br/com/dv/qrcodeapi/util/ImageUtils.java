package br.com.dv.qrcodeapi.util;

import br.com.dv.qrcodeapi.exception.ImageProcessingException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ImageUtils {

    private static final String CANNOT_INSTANTIATE_CLASS_MESSAGE = "ImageUtils class cannot be instantiated";

    private ImageUtils() {
        throw new IllegalStateException(CANNOT_INSTANTIATE_CLASS_MESSAGE);
    }

    public static byte[] writeImageToByteArray(BufferedImage image, String format) {
        try (var outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ImageProcessingException(e);
        }
    }

    public static Graphics2D configureGraphics(BufferedImage image) {
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight());
        return graphics;
    }

}

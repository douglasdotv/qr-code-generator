package br.com.dv.qrcodeapi.service;

import br.com.dv.qrcodeapi.util.ImageUtils;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ImageServiceImpl implements ImageService {

    private static final int IMAGE_WIDTH = 250;
    private static final int IMAGE_HEIGHT = 250;
    private static final String IMAGE_TYPE = "png";

    @Override
    public byte[] generateImage() {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = ImageUtils.configureGraphics(image);

        try {
            return ImageUtils.writeImageToByteArray(image, IMAGE_TYPE);
        } finally {
            graphics.dispose();
        }
    }

}

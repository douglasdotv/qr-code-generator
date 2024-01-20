package br.com.dv.qrcodeapi.service;

import br.com.dv.qrcodeapi.dto.ImageResponse;
import br.com.dv.qrcodeapi.exception.ImageProcessingException;
import br.com.dv.qrcodeapi.exception.InvalidCorrectionLevelException;
import br.com.dv.qrcodeapi.util.ImageUtils;
import br.com.dv.qrcodeapi.validation.QRCodeParameterValidator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    
    private final QRCodeParameterValidator qrCodeParameterValidator;
    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    public QRCodeServiceImpl(QRCodeParameterValidator qrCodeParameterValidator) {
        this.qrCodeParameterValidator = qrCodeParameterValidator;
    }

    @Override
    public ImageResponse generateQRCode(String content, int size, String correction, String format) {
        qrCodeParameterValidator.validate(content, size, correction, format);

        BufferedImage qrCodeImage = generateQRCodeImage(content, size, correction);
        byte[] qrCodeImageData = ImageUtils.writeImageToByteArray(qrCodeImage, format);
        MediaType mediaType = ImageUtils.getMediaTypeForImageFormat(format);

        return new ImageResponse(qrCodeImageData, mediaType);
    }

    private BufferedImage generateQRCodeImage(String content, int size, String correction) {
        Map<EncodeHintType, ?> hints = getQRCodeHints(correction.toUpperCase());
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new ImageProcessingException(e);
        }
    }

    private Map<EncodeHintType, ?> getQRCodeHints(String correction) {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);

        switch (correction) {
            case "L" -> hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            case "M" -> hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            case "Q" -> hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
            case "H" -> hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            default -> throw new InvalidCorrectionLevelException();
        }

        return hints;
    }

}

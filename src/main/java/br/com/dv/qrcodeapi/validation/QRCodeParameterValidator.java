package br.com.dv.qrcodeapi.validation;

import br.com.dv.qrcodeapi.enums.ImageFormat;
import br.com.dv.qrcodeapi.exception.InvalidContentException;
import br.com.dv.qrcodeapi.exception.InvalidCorrectionLevelException;
import br.com.dv.qrcodeapi.exception.InvalidImageFormatException;
import br.com.dv.qrcodeapi.exception.InvalidImageSizeException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class QRCodeParameterValidator {

    private static final int MIN_IMAGE_SIZE = 150;
    private static final int MAX_IMAGE_SIZE = 350;
    private static final Set<ImageFormat> SUPPORTED_IMAGE_FORMATS = EnumSet.allOf(ImageFormat.class);
    private static final Set<ErrorCorrectionLevel> CORRECTION_LEVELS = EnumSet.allOf(ErrorCorrectionLevel.class);

    public void validate(String content, int size, String correction, String format) {
        validateContent(content);
        validateImageSize(size);
        validateCorrectionLevel(correction);
        validateImageFormat(format);
    }

    private void validateContent(String content) {
        if (isContentInvalid(content)) {
            throw new InvalidContentException();
        }
    }

    private void validateImageSize(int size) {
        if (isSizeInvalid(size)) {
            throw new InvalidImageSizeException();
        }
    }

    private void validateCorrectionLevel(String correction) {
        if (isCorrectionLevelInvalid(correction)) {
            throw new InvalidCorrectionLevelException();
        }
    }

    private void validateImageFormat(String format) {
        if (isFormatInvalid(format)) {
            throw new InvalidImageFormatException();
        }
    }

    private boolean isContentInvalid(String content) {
        return content == null || content.isBlank();
    }

    private boolean isSizeInvalid(int size) {
        return size < MIN_IMAGE_SIZE || size > MAX_IMAGE_SIZE;
    }

    private boolean isCorrectionLevelInvalid(String correction) {
        return correction == null ||
                CORRECTION_LEVELS.stream().noneMatch(c -> c.name().equalsIgnoreCase(correction));
    }

    private boolean isFormatInvalid(String format) {
        return format == null ||
                SUPPORTED_IMAGE_FORMATS.stream().noneMatch(f -> f.name().equalsIgnoreCase(format));
    }

}

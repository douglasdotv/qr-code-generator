package br.com.dv.qrcodeapi.service;

import br.com.dv.qrcodeapi.exception.InvalidCorrectionLevelException;
import br.com.dv.qrcodeapi.validation.QRCodeParameterValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class QRCodeServiceTest {

    @Mock
    private QRCodeParameterValidator qrCodeParameterValidator;

    private QRCodeService qrCodeService;

    @BeforeEach
    void setUp() {
        qrCodeService = new QRCodeServiceImpl(qrCodeParameterValidator);
    }

    @Test
    @DisplayName("Should successfully generate QR code image when all parameters are valid")
    void shouldGenerateQRCodeWithValidParameters() {
        doNothing().when(qrCodeParameterValidator).validate(
                "content", 250, "L", "png"
        );
        var qrCode = qrCodeService.generateQRCode("content", 250, "L", "png");
        assertNotNull(qrCode);
        assertNotNull(qrCode.imageData());
        assertTrue(qrCode.imageData().length > 0);
        assertEquals(MediaType.IMAGE_PNG, qrCode.mediaType());
    }

    @ParameterizedTest
    @CsvSource({
            "L,png,image/png",
            "M,jpeg,image/jpeg",
            "Q,gif,image/gif",
            "H,png,image/png"
    })
    @DisplayName("Should generate QR code with correct media type for different format combinations")
    void shouldGenerateQRCodeWithCorrectMediaType(
            String correction, String format, String expectedMediaType
    ) {
        doNothing().when(qrCodeParameterValidator).validate(
                "content", 250, correction, format
        );
        var qrCode = qrCodeService.generateQRCode("content", 250, correction, format);
        assertNotNull(qrCode);
        assertNotNull(qrCode.imageData());
        assertTrue(qrCode.imageData().length > 0);
        assertEquals(MediaType.parseMediaType(expectedMediaType), qrCode.mediaType());
    }

    @Test
    @DisplayName("Should throw InvalidCorrectionLevelException when correction level is not valid")
    void shouldThrowExceptionForInvalidCorrectionLevel() {
        doNothing().when(qrCodeParameterValidator).validate(
                "content", 250, "invalid", "png"
        );
        assertThrows(InvalidCorrectionLevelException.class,
                () -> qrCodeService.generateQRCode("content", 250, "invalid", "png"));
    }

    @Test
    @DisplayName("Should successfully generate QR code with minimum allowed size")
    void shouldGenerateQRCodeWithMinimumSize() {
        doNothing().when(qrCodeParameterValidator).validate(
                "content", 150, "L", "png"
        );
        var qrCode = qrCodeService.generateQRCode("content", 150, "L", "png");
        assertNotNull(qrCode);
        assertNotNull(qrCode.imageData());
        assertTrue(qrCode.imageData().length > 0);
    }

    @Test
    @DisplayName("Should successfully generate QR code with maximum allowed size")
    void shouldGenerateQRCodeWithMaximumSize() {
        doNothing().when(qrCodeParameterValidator).validate(
                "content", 350, "L", "png"
        );
        var qrCode = qrCodeService.generateQRCode("content", 350, "L", "png");
        assertNotNull(qrCode);
        assertNotNull(qrCode.imageData());
        assertTrue(qrCode.imageData().length > 0);
    }

    @Test
    @DisplayName("Should successfully generate QR code when content exceeds typical length")
    void shouldGenerateQRCodeWithLongContent() {
        String longContent = "a".repeat(1000);
        doNothing().when(qrCodeParameterValidator).validate(
                longContent, 250, "L", "png"
        );
        var qrCode = qrCodeService.generateQRCode(longContent, 250, "L", "png");
        assertNotNull(qrCode);
        assertNotNull(qrCode.imageData());
        assertTrue(qrCode.imageData().length > 0);
    }

    @Test
    @DisplayName("Should successfully generate QR code when content contains special characters")
    void shouldGenerateQRCodeWithSpecialCharacters() {
        String specialContent = "Hello! こんにちは! ❤️ #@$%";
        doNothing().when(qrCodeParameterValidator).validate(
                specialContent, 250, "L", "png"
        );
        var qrCode = qrCodeService.generateQRCode(specialContent, 250, "L", "png");
        assertNotNull(qrCode);
        assertNotNull(qrCode.imageData());
        assertTrue(qrCode.imageData().length > 0);
    }

}

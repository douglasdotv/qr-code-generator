package br.com.dv.qrcodeapi.validation;

import br.com.dv.qrcodeapi.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QRCodeParameterValidatorTest {

    private QRCodeParameterValidator qrCodeParameterValidator;

    @BeforeEach
    void setUp() {
        qrCodeParameterValidator = new QRCodeParameterValidator();
    }

    @Test
    @DisplayName("Should successfully validate QR code parameters when all inputs are valid")
    void shouldValidateWithValidParameters() {
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                "content", 250, "L", "png"
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Should throw InvalidContentException when content is null, empty, or blank")
    void shouldThrowExceptionForInvalidContent(String content) {
        assertThrows(InvalidContentException.class, () -> qrCodeParameterValidator.validate(
                content, 250, "L", "png"
        ));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 50, 149, 351, 500})
    @DisplayName("Should throw InvalidImageSizeException when size is outside allowed range")
    void shouldThrowExceptionForInvalidSize(int size) {
        assertThrows(InvalidImageSizeException.class, () -> qrCodeParameterValidator.validate(
                "content", size, "L", "png"
        ));
    }

    @ParameterizedTest
    @ValueSource(ints = {150, 200, 250, 300, 350})
    @DisplayName("Should successfully validate QR code parameters when size is within allowed range")
    void shouldValidateWithValidSize(int size) {
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                "content", size, "L", "png"
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"l", "m", "q", "h", "L", "M", "Q", "H"})
    @DisplayName("Should successfully validate QR code parameters when correction level is valid")
    void shouldValidateWithValidCorrectionLevel(String correction) {
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                "content", 250, correction, "png"
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "X", "Y", "invalid", "1", "LOW", "MEDIUM"})
    @DisplayName("Should throw InvalidCorrectionLevelException when correction level is not L, M, Q, or H")
    void shouldThrowExceptionForInvalidCorrectionLevel(String correction) {
        assertThrows(InvalidCorrectionLevelException.class, () -> qrCodeParameterValidator.validate(
                "content", 250, correction, "png"
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"png", "PNG", "jpeg", "JPEG", "gif", "GIF"})
    @DisplayName("Should successfully validate QR code parameters when format is supported")
    void shouldValidateWithValidFormat(String format) {
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                "content", 250, "L", format
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bmp", "tiff", "webp", "svg", "raw", ""})
    @DisplayName("Should throw InvalidImageFormatException when format is not supported")
    void shouldThrowExceptionForInvalidFormat(String format) {
        assertThrows(InvalidImageFormatException.class, () -> qrCodeParameterValidator.validate(
                "content", 250, "L", format
        ));
    }

    @Test
    @DisplayName("Should successfully validate QR code parameters when content contains special characters")
    void shouldValidateWithSpecialCharacters() {
        String specialContent = "Hello! こんにちは! ❤️ #@$%";
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                specialContent, 250, "L", "png"
        ));
    }

    @Test
    @DisplayName("Should successfully validate QR code parameters when content exceeds typical length")
    void shouldValidateWithLongContent() {
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                "a".repeat(1000), 250, "L", "png"
        ));
    }

    @Test
    @DisplayName("Should successfully validate QR code parameters when content is a URL")
    void shouldValidateWithUrlContent() {
        String urlContent = "https://example.com/path?param1=value1&param2=value2";
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                urlContent, 250, "L", "png"
        ));
    }

    @ParameterizedTest
    @CsvSource({
            "content,250,L,png",
            "https://example.com,350,H,jpeg",
            "test123,150,M,jpeg",
            "hello world,300,Q,gif"
    })
    @DisplayName("Should successfully validate QR code parameters with various valid combinations")
    void shouldValidateWithValidParameterCombinations(
            String content, int size, String correction, String format
    ) {
        assertDoesNotThrow(() -> qrCodeParameterValidator.validate(
                content, size, correction, format
        ));
    }

}

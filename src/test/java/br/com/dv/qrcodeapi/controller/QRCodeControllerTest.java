package br.com.dv.qrcodeapi.controller;

import br.com.dv.qrcodeapi.dto.ImageResponse;
import br.com.dv.qrcodeapi.exception.InvalidContentException;
import br.com.dv.qrcodeapi.exception.InvalidCorrectionLevelException;
import br.com.dv.qrcodeapi.service.QRCodeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(QRCodeController.class)
class QRCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QRCodeService qrCodeService;

    @Test
    @DisplayName("Should successfully generate QR code when all parameters are valid")
    void shouldGenerateQRCodeWithValidParameters() throws Exception {
        var mockResponse = new ImageResponse(new byte[]{1, 2, 3}, MediaType.IMAGE_PNG);
        when(qrCodeService.generateQRCode(anyString(), anyInt(), anyString(), anyString())).thenReturn(mockResponse);
        mockMvc.perform(get("/api/qrcode")
                        .param("contents", "test")
                        .param("size", "250")
                        .param("correction", "L")
                        .param("type", "png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));
    }

    @Test
    @DisplayName("Should successfully generate QR code when only required parameters are provided")
    void shouldGenerateQRCodeWithDefaultParameters() throws Exception {
        var mockResponse = new ImageResponse(new byte[]{1, 2, 3}, MediaType.IMAGE_PNG);
        when(qrCodeService.generateQRCode(anyString(), anyInt(), anyString(), anyString())).thenReturn(mockResponse);
        mockMvc.perform(get("/api/qrcode").param("contents", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when size parameter is not a valid number")
    void shouldReturnBadRequestForInvalidSize() throws Exception {
        mockMvc.perform(get("/api/qrcode")
                        .param("contents", "test")
                        .param("size", "invalid")
                        .param("correction", "L")
                        .param("type", "png"))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"X", "Y", "invalid", "A"})
    @DisplayName("Should return 400 Bad Request when error correction level is not L, M, Q, or H")
    void shouldReturnBadRequestForInvalidCorrectionLevel(String correction) throws Exception {
        when(qrCodeService.generateQRCode(anyString(), anyInt(), eq(correction), anyString()))
                .thenThrow(new InvalidCorrectionLevelException());
        mockMvc.perform(get("/api/qrcode")
                        .param("contents", "test")
                        .param("size", "250")
                        .param("correction", correction)
                        .param("type", "png"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when content parameter is empty")
    void shouldReturnBadRequestForEmptyContent() throws Exception {
        when(qrCodeService.generateQRCode(eq(""), anyInt(), anyString(), anyString()))
                .thenThrow(new InvalidContentException());
        mockMvc.perform(get("/api/qrcode").param("contents", ""))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"jpeg", "gif"})
    @DisplayName("Should successfully generate QR code with specified image format")
    void shouldGenerateQRCodeWithValidFormat(String format) throws Exception {
        MediaType expectedType = switch (format) {
            case "jpeg" -> MediaType.IMAGE_JPEG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.IMAGE_PNG;
        };
        var mockResponse = new ImageResponse(new byte[]{1, 2, 3}, expectedType);
        when(qrCodeService.generateQRCode(anyString(), anyInt(), anyString(), anyString())).thenReturn(mockResponse);
        mockMvc.perform(get("/api/qrcode")
                        .param("contents", "test")
                        .param("type", format))
                .andExpect(status().isOk())
                .andExpect(content().contentType(expectedType));
    }

    @Test
    @DisplayName("Should successfully generate QR code when content contains special characters")
    void shouldGenerateQRCodeWithSpecialCharacters() throws Exception {
        String specialContent = "Hello! こんにちは! ❤️ #@$%";
        var mockResponse = new ImageResponse(new byte[]{1, 2, 3}, MediaType.IMAGE_PNG);
        when(qrCodeService.generateQRCode(anyString(), anyInt(), anyString(), anyString())).thenReturn(mockResponse);
        mockMvc.perform(get("/api/qrcode").param("contents", specialContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    @DisplayName("Should successfully generate QR code when content exceeds typical length")
    void shouldGenerateQRCodeWithLongContent() throws Exception {
        String longContent = "a".repeat(1000);
        var mockResponse = new ImageResponse(new byte[]{1, 2, 3}, MediaType.IMAGE_PNG);
        when(qrCodeService.generateQRCode(anyString(), anyInt(), anyString(), anyString())).thenReturn(mockResponse);
        mockMvc.perform(get("/api/qrcode").param("contents", longContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

}

package br.com.dv.qrcodeapi.controller;

import br.com.dv.qrcodeapi.dto.ImageResponse;
import br.com.dv.qrcodeapi.service.QrCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/qrcode")
@RestController
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    public ResponseEntity<byte[]> generateQrCode(
            @RequestParam(name = "contents") String content,
            @RequestParam(required = false, defaultValue = "250") int size,
            @RequestParam(required = false, defaultValue = "L") String correction,
            @RequestParam(name = "type", required = false, defaultValue = "png") String format
    ) {
        ImageResponse response = qrCodeService.generateQrCode(content, size, correction, format);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(response.mediaType())
                .body(response.imageData());
    }

}

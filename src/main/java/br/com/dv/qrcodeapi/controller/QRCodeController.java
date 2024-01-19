package br.com.dv.qrcodeapi.controller;

import br.com.dv.qrcodeapi.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/qrcode")
@RestController
public class QRCodeController {

    private final ImageService imageService;

    public QRCodeController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<byte[]> generateQRCode() {
        byte[] image = imageService.generateImage();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

}

package br.com.dv.qrcodeapi.controller;

import br.com.dv.qrcodeapi.dto.ImageResponse;
import br.com.dv.qrcodeapi.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/qrcode")
@RestController
public class QRCodeController {

    private final ImageService imageService;

    public QRCodeController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<byte[]> generateQRCode(@RequestParam int size, @RequestParam(name = "type") String format) {
        ImageResponse response = imageService.generateImage(size, format);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(response.mediaType())
                .body(response.imageData());
    }

}

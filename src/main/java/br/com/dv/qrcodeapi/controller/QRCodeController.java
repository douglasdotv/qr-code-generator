package br.com.dv.qrcodeapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/qrcode")
@RestController
public class QRCodeController {

    @GetMapping
    public ResponseEntity<Void> generateQRCode() {
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

}

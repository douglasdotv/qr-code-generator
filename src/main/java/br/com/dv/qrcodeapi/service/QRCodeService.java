package br.com.dv.qrcodeapi.service;

import br.com.dv.qrcodeapi.dto.ImageResponse;

public interface QRCodeService {

    ImageResponse generateQRCode(
            String content,
            int size,
            String correction,
            String format,
            String fcolor,
            String bcolor,
            int margin
    );

}

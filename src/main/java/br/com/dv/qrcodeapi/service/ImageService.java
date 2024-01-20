package br.com.dv.qrcodeapi.service;

import br.com.dv.qrcodeapi.dto.ImageResponse;

public interface ImageService {

    ImageResponse generateImage(int size, String format);

}

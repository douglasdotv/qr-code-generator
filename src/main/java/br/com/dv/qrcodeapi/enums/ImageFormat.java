package br.com.dv.qrcodeapi.enums;

import org.springframework.http.MediaType;

public enum ImageFormat {

    PNG(MediaType.IMAGE_PNG),
    JPEG(MediaType.IMAGE_JPEG),
    GIF(MediaType.IMAGE_GIF);

    private final MediaType mediaType;

    ImageFormat(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

}

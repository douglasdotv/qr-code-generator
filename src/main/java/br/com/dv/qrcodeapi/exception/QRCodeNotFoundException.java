package br.com.dv.qrcodeapi.exception;

public class QRCodeNotFoundException extends RuntimeException {

    private static final String QR_CODE_NOT_FOUND_MESSAGE = "QR code not found";

    public QRCodeNotFoundException() {
        super(QR_CODE_NOT_FOUND_MESSAGE);
    }

}

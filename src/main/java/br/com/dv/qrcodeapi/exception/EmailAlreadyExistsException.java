package br.com.dv.qrcodeapi.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email already exists";

    public EmailAlreadyExistsException() {
        super(EMAIL_ALREADY_EXISTS_MESSAGE);
    }

}

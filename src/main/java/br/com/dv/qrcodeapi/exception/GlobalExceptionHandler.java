package br.com.dv.qrcodeapi.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchException(TypeMismatchException e) {
        String errorMessage = String.format(
                "Invalid parameter value '%s' for '%s'",
                e.getValue(),
                e.getPropertyName()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(errorMessage));
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ApiError> handleImageProcessingException(ImageProcessingException e) {
        return getResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidImageSizeException.class)
    public ResponseEntity<ApiError> handleInvalidImageSizeException(InvalidImageSizeException e) {
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidImageFormatException.class)
    public ResponseEntity<ApiError> handleInvalidImageFormatException(InvalidImageFormatException e) {
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidContentException.class)
    public ResponseEntity<ApiError> handleInvalidContentException(InvalidContentException e) {
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCorrectionLevelException.class)
    public ResponseEntity<ApiError> handleInvalidCorrectionLevelException(InvalidCorrectionLevelException e) {
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> getResponseEntity(Exception e, HttpStatus status) {
        ApiError error = new ApiError(e.getMessage());
        return ResponseEntity.status(status).body(error);
    }

    public record ApiError(String error) {}

}

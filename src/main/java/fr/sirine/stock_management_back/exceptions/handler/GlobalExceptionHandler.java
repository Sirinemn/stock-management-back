package fr.sirine.stock_management_back.exceptions.handler;

import fr.sirine.stock_management_back.exceptions.custom.EmailAlreadyUsedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static fr.sirine.stock_management_back.exceptions.handler.BusinessErrorCodes.EMAIL_ALREADY_USED;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ExceptionResponse> handleException(EmailAlreadyUsedException exp) {
        return ResponseEntity
                .status(EMAIL_ALREADY_USED.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .errorCode(EMAIL_ALREADY_USED.getCode())
                                .message(EMAIL_ALREADY_USED.getDescription())
                                .httpStatus(EMAIL_ALREADY_USED.getHttpStatus().value())
                                .build()
                );
    }
}

package fr.sirine.stock_management_back.exceptions.handler;

import fr.sirine.stock_management_back.exceptions.custom.EmailAlreadyUsedException;
import fr.sirine.stock_management_back.exceptions.custom.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static fr.sirine.stock_management_back.exceptions.handler.BusinessErrorCodes.EMAIL_ALREADY_USED;
import static fr.sirine.stock_management_back.exceptions.handler.BusinessErrorCodes.USER_NOT_FOUND;

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
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException exp) {
        return ResponseEntity
                .status(USER_NOT_FOUND.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .errorCode(USER_NOT_FOUND.getCode())
                                .message(USER_NOT_FOUND.getDescription())
                                .httpStatus(USER_NOT_FOUND.getHttpStatus().value())
                                .build()
                );
    }
}

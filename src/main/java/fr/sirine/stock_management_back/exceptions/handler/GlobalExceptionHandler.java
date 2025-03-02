package fr.sirine.stock_management_back.exceptions.handler;

import fr.sirine.stock_management_back.exceptions.custom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ExceptionResponse> buildResponseEntity(BusinessErrorCodes errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .errorCode(errorCode.getCode())
                                .message(message)
                                .httpStatus(errorCode.getHttpStatus().value())
                                .build()
                );
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ExceptionResponse> handleException(EmailAlreadyUsedException exp) {
        return buildResponseEntity(BusinessErrorCodes.EMAIL_ALREADY_USED, exp.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException exp) {
        return buildResponseEntity(BusinessErrorCodes.USER_NOT_FOUND, exp.getMessage());
    }
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleException(JwtTokenExpiredException exp) {
        return buildResponseEntity(BusinessErrorCodes.JWT_TOKEN_EXPIRED, exp.getMessage());
    }
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ExceptionResponse> handleException(UnauthorizedActionException exp) {
        return buildResponseEntity(BusinessErrorCodes.UNAUTHORIZED_ACTION, exp.getMessage());
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(RoleNotFoundException exp) {
        return buildResponseEntity(BusinessErrorCodes.ROLE_NOT_FOUND, exp.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        logger.error("An unexpected error occurred", exp);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .message("Internal error, please contact the admin")
                                .errorCode(INTERNAL_SERVER_ERROR.value())
                                .build()
                );
    }
}

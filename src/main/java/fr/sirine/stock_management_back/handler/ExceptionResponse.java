package fr.sirine.stock_management_back.handler;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ExceptionResponse {
    private int errorCode;
    private String message;
    private int httpStatus;

    public ExceptionResponse(int errorCode, String message, int httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

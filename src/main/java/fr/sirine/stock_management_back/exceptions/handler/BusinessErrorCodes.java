package fr.sirine.stock_management_back.exceptions.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    USER_NOT_FOUND(1001, "Utilisateur non trouvé", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_USED(1002, "Email déjà utilisé", HttpStatus.CONFLICT),
    UNAUTHORIZED_ACTION(1003, "Action non autorisée", HttpStatus.FORBIDDEN),
    INVALID_REQUEST(1004, "Requête invalide", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1005, "Erreur interne du serveur", HttpStatus.INTERNAL_SERVER_ERROR),
    JWT_TOKEN_EXPIRED(1006, "Session expiré", HttpStatus.UNAUTHORIZED);


    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

}

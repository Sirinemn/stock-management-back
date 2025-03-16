package fr.sirine.stock_management_back.exceptions.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Getter
public enum BusinessErrorCodes {

    USER_NOT_FOUND(1001, "Utilisateur non trouvé", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_USED(1002, "Email déjà utilisé", HttpStatus.CONFLICT),
    UNAUTHORIZED_ACTION(1003, "Action non autorisée", HttpStatus.FORBIDDEN),
    INVALID_REQUEST(1004, "Requête invalide", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1005, "Erreur interne du serveur", HttpStatus.INTERNAL_SERVER_ERROR),
    JWT_TOKEN_EXPIRED(1006, "Session expiré", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND(1007, "Rôle non trouvé", HttpStatus.NOT_FOUND),
    BAD_CREDENTIALS(1008, "Login and / or Password is incorrect", FORBIDDEN),
    CATEGORY_NOT_FOUND(1009, "Catégorie non trouvée", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(1010, "Produit non trouvé", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK(1011, "Stock insuffisant", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_EXIST(1012, "Catégorie déjà utilisée", HttpStatus.CONFLICT),
    EMAIL_SERVICE_ERROR(1013, "Erreur du service d'email", HttpStatus.INTERNAL_SERVER_ERROR);


    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

}

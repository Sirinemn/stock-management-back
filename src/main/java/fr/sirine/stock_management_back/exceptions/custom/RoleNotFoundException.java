package fr.sirine.stock_management_back.exceptions.custom;
import fr.sirine.stock_management_back.exceptions.handler.BusinessErrorCodes;


public class RoleNotFoundException extends RuntimeException {
    private final BusinessErrorCodes businessErrorCodes;

    public RoleNotFoundException(BusinessErrorCodes businessErrorCodes) {
        super(businessErrorCodes.getDescription());
        this.businessErrorCodes = businessErrorCodes;
    }

    public BusinessErrorCodes getErrorCode() {
        return businessErrorCodes;
    }
}

package fr.sirine.stock_management_back.exceptions.custom;
import fr.sirine.stock_management_back.exceptions.handler.BusinessErrorCodes;


public class RoleNotFoundException extends RuntimeException {
    private final BusinessErrorCodes businessErrorCodes;

    public RoleNotFoundException(String roleName) {
        super("Role not found: " + roleName);
        this.businessErrorCodes = BusinessErrorCodes.USER_NOT_FOUND;
    }

    public BusinessErrorCodes getErrorCode() {
        return businessErrorCodes;
    }
}

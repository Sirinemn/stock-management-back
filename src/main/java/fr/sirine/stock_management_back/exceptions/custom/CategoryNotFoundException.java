package fr.sirine.stock_management_back.exceptions.custom;

import fr.sirine.stock_management_back.exceptions.handler.BusinessErrorCodes;

public class CategoryNotFoundException extends RuntimeException {

    private final BusinessErrorCodes businessErrorCodes;
    public CategoryNotFoundException() {
        super(BusinessErrorCodes.CATEGORY_NOT_FOUND.getDescription());
        this.businessErrorCodes = BusinessErrorCodes.CATEGORY_NOT_FOUND;
    }
    public BusinessErrorCodes getErrorCode() {
        return businessErrorCodes;
    }

}

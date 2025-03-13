package fr.sirine.stock_management_back.exceptions.custom;

import fr.sirine.stock_management_back.exceptions.handler.BusinessErrorCodes;

public class CategoryAlreadyExistException extends RuntimeException {
    private final BusinessErrorCodes businessErrorCodes;

    public CategoryAlreadyExistException(BusinessErrorCodes businessErrorCodes) {
        super(businessErrorCodes.getDescription());
        this.businessErrorCodes = businessErrorCodes;
    }
}

package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.entities.StockMovement;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.service.impl.ProductService;
import fr.sirine.stock_management_back.service.impl.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", imports = {Product.class})
public abstract class StockMovementMapper implements EntityMapper<StockMovementDto, StockMovement> {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Mapping(target = "productId", expression = "java(stockMovement.getProduct().getId())")
    @Mapping(target = "userId", expression = "java(stockMovement.getUser().getId())")
    public abstract StockMovementDto toDto(StockMovement stockMovement);

    @Mapping(target = "product", source = "productId", qualifiedByName = "findProductById")
    @Mapping(target = "user", source = "userId", qualifiedByName = "findUserById")
    public abstract StockMovement toEntity(StockMovementDto stockMovementDto);

    @Named("findProductById")
    Product findProductById(Integer productId) {
        return productService.findById(productId);
    }
    @Named("findUserById")
    User findUserById(Integer userId) {
        return userService.findById(userId);
    }
}

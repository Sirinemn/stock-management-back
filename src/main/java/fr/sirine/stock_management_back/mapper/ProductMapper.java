package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class ProductMapper implements EntityMapper<ProductDto, Product> {

    @Autowired
    UserService userService;

    @Mapping(target = "userId", expression = "java(product.getUser().getId())")
    public abstract ProductDto toDto(Product product);

    @Mapping(target = "user", qualifiedByName = "findUserById")
    public abstract Product toEntity(ProductDto productDto);

    @Named("findUserById")
    User findUserById(Integer userId) {
        return userService.findById(userId);
    }
}

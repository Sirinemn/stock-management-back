package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.service.CategoryService;
import fr.sirine.stock_management_back.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", imports = {User.class})
public abstract class ProductMapper implements EntityMapper<ProductDto, Product> {

    @Autowired
    UserService userService;
    @Autowired
    private CategoryService categoryService;

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    public abstract ProductDto toDto(Product product);

    @Mapping(source = "userId", target = "user", qualifiedByName = "findUserById")
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "findCategoryById")
    public abstract Product toEntity(ProductDto productDto);

    @Named("findUserById")
    User findUserById(Integer userId) {
        return userService.findById(userId);
    }
    @Named("findCategoryById")
    Category findCategoryById(Integer categoryId) {
        return categoryService.findById(categoryId);
    }
}

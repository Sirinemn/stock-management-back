package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.service.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class StockMovementMapper {

    @Autowired
    private ProductService productService;

    @Mapping(target = "productId", expression = "java(product.getId())")
    public abstract ProductDto toDto(Product product);

    @Mapping(target = "product", qualifiedByName = "findProductById")
    public abstract Product toEntity(ProductDto productDto);

    @Named("findProductById")
    Product findProductById(Integer productId) {
        return productService.findById(productId);
    }
}

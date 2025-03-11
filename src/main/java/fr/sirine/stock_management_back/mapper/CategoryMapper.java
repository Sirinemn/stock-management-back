package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Mapper(componentModel = "spring")
public abstract class CategoryMapper implements EntityMapper<CategoryDto, Category>{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract CategoryDto toDto(Category entity);

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract Category toEntity(CategoryDto dto);

}

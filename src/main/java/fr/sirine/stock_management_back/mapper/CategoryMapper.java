package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.entities.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Mapper(componentModel = "spring")
public abstract class CategoryMapper implements EntityMapper<CategoryDto, Category>{
    @Override
    public CategoryDto toDto(Category entity) {
        return null;
    }

    @Override
    public Category toEntity(CategoryDto dto) {
        return null;
    }


}

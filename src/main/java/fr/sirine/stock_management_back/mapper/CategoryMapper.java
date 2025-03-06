package fr.sirine.stock_management_back.mapper;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.entities.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public class CategoryMapper implements EntityMapper<Category, CategoryDto>{
    @Override
    public Category toDto(CategoryDto entity) throws IOException {
        return null;
    }

    @Override
    public CategoryDto toEntity(Category dto) {
        return null;
    }

    @Override
    public List<CategoryDto> toEntity(List<Category> dtoList) {
        return null;
    }

    @Override
    public List<Category> toDto(List<CategoryDto> entityList) {
        return null;
    }
}

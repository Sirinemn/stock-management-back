package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.exceptions.custom.CategoryNotFoundException;
import fr.sirine.stock_management_back.mapper.CategoryMapper;
import fr.sirine.stock_management_back.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::toDto).toList();
    }
    public void addCategory(String categoryName) {
        Category category = new Category(categoryName);
        categoryRepository.save(category);
    }
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }
    public CategoryDto getByName(String name) {
        Category category = categoryRepository.findByName(name);
        return categoryMapper.toDto(category);
    }
}

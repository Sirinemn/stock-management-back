package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public void addCategory(String categoryName) {
        Category category = new Category(categoryName);
        categoryRepository.save(category);
    }
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}

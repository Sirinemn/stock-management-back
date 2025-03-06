package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category[] getAllCategories() {
        return categoryRepository.findAll().toArray(new Category[0]);
    }
    public void addCategory(String categoryName) {
        Category category = new Category(categoryName);
        categoryRepository.save(category);
    }
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}

package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.exceptions.custom.CategoryAlreadyExistException;
import fr.sirine.stock_management_back.exceptions.custom.CategoryNotFoundException;
import fr.sirine.stock_management_back.exceptions.custom.IllegalStateException;
import fr.sirine.stock_management_back.mapper.CategoryMapper;
import fr.sirine.stock_management_back.repository.CategoryRepository;
import fr.sirine.stock_management_back.service.ICategoryService;
import fr.sirine.stock_management_back.service.IProductService;
import fr.sirine.stock_management_back.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final IUserService userService;
    private final IProductService productService;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, IUserService userService, IProductService productService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.userService = userService;
        this.productService = productService;
    }

    public List<CategoryDto> getAllCategories(Integer groupId) {
        List<Category> categories = categoryRepository.findByGroupId(groupId);
        return categories.stream().map(categoryMapper::toDto).toList();
    }
    public void addCategory(String categoryName, Integer userId) {
        User user = userService.findById(userId);
        if (categoryRepository.findByNameAndGroupId(categoryName, user.getGroup().getId()).isPresent()) {
            throw new CategoryAlreadyExistException();
        }else {
            Category category = new Category(categoryName);
            category.setGroup(user.getGroup());
            categoryRepository.save(category);
        }
    }
    public void deleteCategory(Integer id, Integer groupId) {
        categoryRepository.deleteById(id);
        boolean hasProducts = productService.existsByCategoryIdAndGroupId(id, groupId);
        if (hasProducts) {
            throw new IllegalStateException("Impossible de supprimer la catégorie car elle contient des produits.");
        }
        categoryRepository.deleteById(id);
    }
    public Category findById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }

}

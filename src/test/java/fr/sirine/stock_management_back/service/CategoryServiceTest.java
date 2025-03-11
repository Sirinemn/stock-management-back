package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.CategoryDto;
import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.mapper.CategoryMapper;
import fr.sirine.stock_management_back.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1)
                .name("category")
                .build();
        categoryDto = CategoryDto.builder()
                .id(1)
                .name("category")
                .build();
    }
    @Test
    void should_get_all_categories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        categoryService.getAllCategories();
        verify(categoryRepository, times(1)).findAll();
    }
    @Test
    void should_add_category() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        categoryService.addCategory("category");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    @Test
    void should_delete_category() {
        categoryService.deleteCategory(1);
        verify(categoryRepository, times(1)).deleteById(1);
    }
    @Test
    void should_find_category_by_id() {
        when(categoryRepository.findById(1)).thenReturn(java.util.Optional.of(category));
        categoryService.findById(1);
        verify(categoryRepository, times(1)).findById(1);
    }
    @Test
    void should_find_category_by_name() {
        when(categoryRepository.findByName("category")).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        categoryService.getByName("category");
        verify(categoryRepository, times(1)).findByName("category");
    }
}

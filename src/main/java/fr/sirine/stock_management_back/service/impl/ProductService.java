package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.exceptions.custom.ProductNotFoundException;
import fr.sirine.stock_management_back.mapper.ProductMapper;
import fr.sirine.stock_management_back.repository.ProductRepository;
import fr.sirine.stock_management_back.service.ICategoryService;
import fr.sirine.stock_management_back.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final IUserService userService;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          ICategoryService categoryService,
                          IUserService userService,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productMapper = productMapper;
    }

    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryService.findById(productDto.getCategoryId());
        User user = userService.findById(productDto.getUserId());

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        product.setUser(user);

        return productMapper.toDto(productRepository.save(product));
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(ProductNotFoundException::new);
        assert product != null;
        return productMapper.toDto(productRepository.save(product));
    }
}

package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.entities.Product;

public interface IProductService {
    ProductDto createProduct(ProductDto productDto);
    Product findById(Integer id);
    void deleteProduct(Integer id);
    ProductDto updateProduct(ProductDto productDto);
}


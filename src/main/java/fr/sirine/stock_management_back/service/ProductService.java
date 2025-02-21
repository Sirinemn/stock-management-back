package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
}

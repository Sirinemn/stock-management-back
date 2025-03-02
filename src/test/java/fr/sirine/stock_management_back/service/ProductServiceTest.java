package fr.sirine.stock_management_back.service;


import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @Test
    void should_find_product_by_id() {
        Product initialProduct = Product.builder()
                .id(1)
                .name("product")
                .price(10.0)
                .quantity(10)
                .build();
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(initialProduct));
        productService.findById(1);
        verify(productRepository, times(1)).findById(1);
    }
}

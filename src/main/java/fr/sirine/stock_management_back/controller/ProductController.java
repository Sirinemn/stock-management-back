package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.mapper.ProductMapper;
import fr.sirine.stock_management_back.service.impl.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Management", description = "Operations related to product management")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Operation(summary = "Create a new product", description = "Adds a new product to the system")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @Operation(summary = "Update a product", description = "Update a product's details")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductDto productDto) {
        productDto.setId(id);
        ProductDto updatedProduct = productService.updateProduct(productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}


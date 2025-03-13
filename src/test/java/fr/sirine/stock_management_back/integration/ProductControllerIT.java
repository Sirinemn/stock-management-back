package fr.sirine.stock_management_back.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.repository.CategoryRepository;
import fr.sirine.stock_management_back.repository.ProductRepository;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;
    private User user;

    @BeforeAll
    void setup() {
        user = userRepository.save(User.builder()
                .email("john@doe.fr")
                .password("password")
                .firstname("John")
                .lastname("Doe")
                .dateOfBirth(LocalDateTime.now())
                .build());
        category = categoryRepository.save(Category.builder()
                .name("Electronics")
                .build());
    }

    @Test
    @DisplayName("Should create a product successfully")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void shouldCreateProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name("Laptop")
                .description("High-end laptop")
                .quantity(5)
                .price(1200.99)
                .userId(user.getId())
                .categoryId(category.getId())
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200.99));
    }

    @Test
    @DisplayName("Should get a product by ID")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void shouldGetProductById() throws Exception {
        Product product = productRepository.save(Product.builder()
                .name("Smartphone")
                .description("Flagship phone")
                .quantity(10)
                .price(899.99)
                .user(user)
                .category(category)
                .build());

        mockMvc.perform(get("/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Smartphone"))
                .andExpect(jsonPath("$.price").value(899.99));
    }

    @Test
    @DisplayName("Should update a product successfully")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void shouldUpdateProduct() throws Exception {
        Product product = productRepository.save(Product.builder()
                .name("Tablet")
                .description("Powerful tablet")
                .quantity(3)
                .price(499.99)
                .user(user)
                .category(category)
                .build());

        ProductDto updatedProductDto = ProductDto.builder()
                .id(product.getId())
                .name("Updated Tablet")
                .description("New generation tablet")
                .quantity(4)
                .price(599.99)
                .userId(user.getId())
                .categoryId(category.getId())
                .build();

        mockMvc.perform(put("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Tablet"))
                .andExpect(jsonPath("$.price").value(599.99));
    }

    @Test
    @DisplayName("Should delete a product successfully")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void shouldDeleteProduct() throws Exception {
        Product product = productRepository.save(Product.builder()
                .name("Headphones")
                .description("Noise-cancelling headphones")
                .quantity(2)
                .price(199.99)
                .user(user)
                .category(category)
                .build());

        mockMvc.perform(delete("/products/" + product.getId()))
                .andExpect(status().isNoContent());

        // Vérification que le produit a bien été supprimé
        assertFalse(productRepository.findById(product.getId()).isPresent());
    }
}


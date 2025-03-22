package fr.sirine.stock_management_back.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.entities.*;
import fr.sirine.stock_management_back.repository.CategoryRepository;
import fr.sirine.stock_management_back.repository.ProductRepository;
import fr.sirine.stock_management_back.repository.StockMovementRepository;
import fr.sirine.stock_management_back.repository.UserRepository;
import fr.sirine.stock_management_back.service.impl.StockAlertService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StockMovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;
    private User user;
    private Category category;
    private Group group;

    @BeforeEach
    void setup() {
        group = Group.builder()
                .id(1)
                .name("group")
                .build();
        category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        user = new User();
        user.setEmail("john@doe.fr");
        user.setPassword("password");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setDateOfBirth(LocalDateTime.now());
        userRepository.save(user);

        product = new Product();
        product.setName("Laptop");
        product.setDescription("Powerful Laptop");
        product.setQuantity(10);
        product.setPrice(1200.0);
        product.setUser(user);
        product.setCategory(category);
        productRepository.save(product);
    }

    @AfterEach
    void cleanUp() {
        stockMovementRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Ajout d'une entrée de stock")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void testAddStockMovementEntry() throws Exception {
        StockMovementDto stockMovementDto = new StockMovementDto(null, product.getId(), user.getId(),"ENTREE", 5, LocalDateTime.now(),1);

        mockMvc.perform(post("/stocks/movement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockMovementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mouvement de stock ajouté avec succès"));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(15, updatedProduct.getQuantity()); // 10 + 5 = 15
    }

    @Test
    @DisplayName("Ajout d'une sortie de stock")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void testAddStockMovementExit() throws Exception {
        StockMovementDto stockMovementDto = new StockMovementDto(null, product.getId(), user.getId(),"ENTREE", 5, LocalDateTime.now(),1);

        mockMvc.perform(post("/stocks/movement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockMovementDto)))
                .andExpect(status().isOk());

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(7, updatedProduct.getQuantity()); // 10 - 3 = 7
    }

    @Test
    @DisplayName("Ajout d'une sortie de stock insuffisante")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void testAddStockMovementExitInsufficientStock() throws Exception {
        StockMovementDto stockMovementDto = new StockMovementDto(null, product.getId(), user.getId(),"ENTREE", 5, LocalDateTime.now(),1);

        mockMvc.perform(post("/stocks/movement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockMovementDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Récupération des mouvements de stock d'un produit")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void testGetStockMovements() throws Exception {
        stockMovementRepository.save(new StockMovement(null,product, StockMovement.TypeMovement.ENTREE, 5, LocalDateTime.now(), user, group));
        stockMovementRepository.save(new StockMovement(null, product, StockMovement.TypeMovement.SORTIE, 2, LocalDateTime.now(), user, group));

        mockMvc.perform(get("/stocks/movements/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    @DisplayName("Récupérer l'historique des mouvements de stock par utilisateur")
    @WithMockUser(username = "mockUser", authorities = {"USER"})
    void testGetStockMovementsByUser() throws Exception {
        User user = new User();
        user.setFirstname("admin");
        user.setPassword("password");
        userRepository.save(user);

        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("High-end laptop");
        product.setQuantity(10);
        productRepository.save(product);

        StockMovement stockMovement = new StockMovement(null, product, StockMovement.TypeMovement.ENTREE, 5, LocalDateTime.now(), user,group);
        stockMovementRepository.save(stockMovement);

        mockMvc.perform(get("/stocks/history")
                        .param("userId", user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(user.getId()))
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

}

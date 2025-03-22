package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.entities.*;
import fr.sirine.stock_management_back.repository.StockMovementRepository;
import fr.sirine.stock_management_back.service.impl.StockAlertService;
import fr.sirine.stock_management_back.service.impl.StockMovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockMovementServiceTest {
    @InjectMocks
    private StockMovementService stockMovementService;
    @Mock
    private StockMovementRepository stockMovementRepository;
    @Mock
    private IProductService productService;
    @Mock
    private IUserService userService;
    @Mock
    private StockAlertService stockAlertService;

    StockMovementDto stockMovementDto;
    Product product;
    User user;
    Category category;
    Group group;

    @BeforeEach
    public void setUp() {
        group = Group.builder()
                .id(1)
                .name("group")
                .build();
        category = Category.builder()
                .name("category")
                .build();
        user = User.builder()
                .firstname("user")
                .lastname("user")
                .password("password")
                .email("email")
                .build();
        product = Product.builder()
                .name("product")
                .quantity(10)
                .price(100)
                .user(user)
                .category(category)
                .description("description")
                .build();
        stockMovementDto = StockMovementDto.builder()
                .userId(1)
                .productId(1)
                .quantity(5)
                .type("ENTREE")
                .build();
    }
    @Test
    public void addStockMovementTest() {
        when(productService.findById(1)).thenReturn(product);
        when(userService.findById(1)).thenReturn(user);
        when(productService.updateProduct(any(ProductDto.class))).thenReturn(new ProductDto());
        doNothing().when(stockAlertService).checkStockLevel(any(Product.class));

        stockMovementService.addStockMovement(stockMovementDto);

        verify(stockMovementRepository, times(1)).save(any(StockMovement.class));
        verify(stockAlertService, times(1)).checkStockLevel(any(Product.class));
        verify(productService, times(1)).updateProduct(any(ProductDto.class));
    }
    @Test
    public void getStockMovementsByProductTest() {
        stockMovementService.getStockMovementsByProduct(1,1);

        verify(stockMovementRepository, times(1)).findAllByProductId(1);
    }
    @Test
    public void getStockMovementsTest() {
        stockMovementService.getStockMovements(1, 1, 1, null, null);

        verify(stockMovementRepository, times(1)).findByUserId(1);
        verify(stockMovementRepository, times(0)).findAllByProductId(1);
    }
}

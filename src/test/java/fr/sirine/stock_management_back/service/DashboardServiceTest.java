package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.DashboardOverviewDto;
import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.service.impl.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {
    @InjectMocks
    private DashboardService dashboardService;
    @Mock
    private IProductService productService;
    @Mock
    private IStockMovementService stockMovementService;

    @Test
    void shouldReturnDashboardOverview() {
        // Given
        StockMovementDto stockMovementDto = StockMovementDto.builder()
                .userId(1)
                .productId(1)
                .quantity(1)
                .date(LocalDateTime.now())
                .type("SORTIE")
                .build();
        DashboardOverviewDto dashboardOverviewDto = DashboardOverviewDto.builder()
                .totalProducts(1)
                .lowStockProducts(1)
                .recentMovements(List.of(stockMovementDto))
                .build();
        // When
        when(productService.count()).thenReturn(1L);
        when(productService.countByQuantityLessThan(5)).thenReturn(1L);
        when(stockMovementService.findTop10ByOrderByDateDesc()).thenReturn(List.of(stockMovementDto));
        // Then
        DashboardOverviewDto result = dashboardService.getDashboardOverview();
        assertEquals(1, result.getTotalProducts());
    }
}
